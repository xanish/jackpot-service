package io.github.xanish.jackpot.service;

import io.github.xanish.jackpot.exception.JackpotNotFoundException;
import io.github.xanish.jackpot.exception.StrategyNotFoundException;
import io.github.xanish.jackpot.model.Bet;
import io.github.xanish.jackpot.model.Jackpot;
import io.github.xanish.jackpot.model.JackpotContribution;
import io.github.xanish.jackpot.model.JackpotReward;
import io.github.xanish.jackpot.repository.JackpotContributionRepository;
import io.github.xanish.jackpot.repository.JackpotRepository;
import io.github.xanish.jackpot.repository.JackpotRewardRepository;
import io.github.xanish.jackpot.strategy.JackpotStrategyResolver;
import io.github.xanish.jackpot.strategy.contribution.ContributionStrategy;
import io.github.xanish.jackpot.strategy.reward.RewardStrategy;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JackpotService {

    private static final Logger logger = LoggerFactory.getLogger(
        JackpotService.class
    );

    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository contributionRepository;
    private final JackpotRewardRepository rewardRepository;
    private final JackpotStrategyResolver strategyResolver;

    @Autowired
    public JackpotService(
        JackpotRepository jackpotRepository,
        JackpotContributionRepository contributionRepository,
        JackpotRewardRepository rewardRepository,
        JackpotStrategyResolver strategyResolver
    ) {
        this.jackpotRepository = jackpotRepository;
        this.contributionRepository = contributionRepository;
        this.rewardRepository = rewardRepository;
        this.strategyResolver = strategyResolver;
    }

    @Transactional
    public void processBetContribution(Bet bet) {
        logger.info("Processing contribution for bet: {}", bet.getBetId());

        // Fetch the associated jackpot for this bet
        Jackpot jackpot = jackpotRepository
            .findById(bet.getJackpotId())
            .orElseThrow(() ->
                new JackpotNotFoundException(
                    "Jackpot with ID " +
                    bet.getJackpotId() +
                    " not found for bet " +
                    bet.getBetId()
                )
            );

        // Resolve the appropriate contribution strategy based on jackpot type
        String contributionStrategyName = jackpot.getContributionType().name();
        ContributionStrategy contributionStrategy = strategyResolver
            .getContributionStrategy(contributionStrategyName)
            .orElseThrow(() -> {
                String errorMessage =
                    "Unsupported contribution strategy: " +
                    contributionStrategyName +
                    " for jackpot ID: " +
                    jackpot.getJackpotId();
                logger.error(errorMessage);
                return new StrategyNotFoundException(errorMessage);
            });

        // Calculate the contribution amount using the resolved strategy
        BigDecimal contributionAmount =
            contributionStrategy.calculateContribution(bet, jackpot);

        jackpot.setCurrentPoolAmount(
            jackpot.getCurrentPoolAmount().add(contributionAmount)
        );
        jackpotRepository.save(jackpot);
        // Update the jackpot's current pool with the new contribution
        JackpotContribution contribution = new JackpotContribution();

        contribution.setBetId(bet.getBetId());
        contribution.setUserId(bet.getUserId());
        contribution.setJackpotId(bet.getJackpotId());
        contribution.setStakeAmount(bet.getBetAmount());
        contribution.setContributionAmount(contributionAmount);
        contribution.setCurrentJackpotAmountAfterContribution(
            jackpot.getCurrentPoolAmount()
        );
        contributionRepository.save(contribution);
        logger.info(
            "Contribution processed for bet {}: Amount={}, New Pool={}",
            bet.getBetId(),
            contributionAmount,
            jackpot.getCurrentPoolAmount()
        );
    }

    @Transactional
    public Optional<JackpotReward> evaluateBetForReward(
        String betId,
        String userId,
        String jackpotId
    ) {
        logger.info(
            "Evaluating reward for betId: {}, jackpotId: {}",
            betId,
            jackpotId
        );

        // Fetch the original contribution record for the bet
        Optional<JackpotContribution> contributionOpt =
            contributionRepository.findByBetId(betId);
        if (contributionOpt.isEmpty()) {
            logger.warn(
                "No jackpot contribution found for betId: {}. Cannot evaluate for reward.",
                betId
            );
            return Optional.empty();
        }
        JackpotContribution contribution = contributionOpt.get();

        // Fetch the jackpot associated with the contribution.
        Jackpot jackpot = jackpotRepository
            .findById(contribution.getJackpotId())
            .orElseThrow(() ->
                new JackpotNotFoundException(
                    "Jackpot with ID " +
                    contribution.getJackpotId() +
                    " not found for reward evaluation of bet " +
                    betId
                )
            );

        // Resolve the reward strategy
        String rewardStrategyName = jackpot.getRewardType().name();
        RewardStrategy rewardStrategy = strategyResolver
            .getRewardStrategy(rewardStrategyName)
            .orElseThrow(() -> {
                String errorMessage =
                    "Unsupported reward strategy: " +
                    rewardStrategyName +
                    " for jackpot ID: " +
                    jackpot.getJackpotId();
                logger.error(errorMessage);
                return new StrategyNotFoundException(errorMessage);
            });

        // Create the Bet object for evaluation
        Bet betForEvaluation = new Bet(
            contribution.getBetId(),
            contribution.getUserId(),
            contribution.getJackpotId(),
            contribution.getStakeAmount()
        );

        //  Evaluate the bet and process the outcome
        if (rewardStrategy.shouldReward(betForEvaluation, jackpot)) {
            BigDecimal rewardAmount = rewardStrategy.getRewardAmount(jackpot);
            logger.info(
                "Bet {} WON jackpot {}! Reward Amount: {}",
                betId,
                jackpotId,
                rewardAmount
            );

            JackpotReward reward = new JackpotReward();
            reward.setBetId(betId);
            reward.setUserId(userId);
            reward.setJackpotId(jackpotId);
            reward.setJackpotRewardAmount(rewardAmount);
            rewardRepository.save(reward);

            // Reset jackpot pool to initial value
            jackpot.setCurrentPoolAmount(jackpot.getInitialPoolAmount());
            jackpotRepository.save(jackpot);
            logger.info(
                "Jackpot {} pool reset to: {}",
                jackpotId,
                jackpot.getInitialPoolAmount()
            );

            return Optional.of(reward);
        } else {
            logger.info("Bet {} did not win jackpot {}.", betId, jackpotId);
            return Optional.empty();
        }
    }
}
