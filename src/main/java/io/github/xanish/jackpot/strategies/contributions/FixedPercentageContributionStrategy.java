package io.github.xanish.jackpot.strategies.contributions;

import io.github.xanish.jackpot.models.Bet;
import io.github.xanish.jackpot.models.Jackpot;
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
