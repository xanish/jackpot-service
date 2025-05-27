package io.github.xanish.jackpot.strategies;

import io.github.xanish.jackpot.strategies.contributions.ContributionStrategy;
import io.github.xanish.jackpot.strategies.rewards.RewardStrategy;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JackpotStrategyResolver {

    private final Map<String, ContributionStrategy> contributionStrategies;
    private final Map<String, RewardStrategy> rewardStrategies;

    @Autowired
    public JackpotStrategyResolver(
        Map<String, ContributionStrategy> contributionStrategies,
        Map<String, RewardStrategy> rewardStrategies
    ) {
        this.contributionStrategies = contributionStrategies;
        this.rewardStrategies = rewardStrategies;
    }

    public Optional<ContributionStrategy> getContributionStrategy(
        String strategyName
    ) {
        return Optional.ofNullable(contributionStrategies.get(strategyName));
    }

    public Optional<RewardStrategy> getRewardStrategy(String strategyName) {
        return Optional.ofNullable(rewardStrategies.get(strategyName));
    }
}
