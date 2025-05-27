package io.github.xanish.jackpot.strategy.contributions;

import io.github.xanish.jackpot.model.Bet;
import io.github.xanish.jackpot.model.Jackpot;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component("FIXED_PERCENTAGE_CONTRIBUTION")
public class FixedPercentageContributionStrategy
    implements ContributionStrategy {

    @Override
    public BigDecimal calculateContribution(Bet bet, Jackpot jackpot) {
        return bet
            .getBetAmount()
            .multiply(jackpot.getContributionValue())
            .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getStrategyName() {
        return "FIXED_PERCENTAGE_CONTRIBUTION";
    }
}
