package io.github.xanish.jackpot.strategies.contributions;

import io.github.xanish.jackpot.models.Bet;
import io.github.xanish.jackpot.models.Jackpot;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component("VARIABLE_PERCENTAGE_CONTRIBUTION")
public class VariablePercentageContributionStrategy
    implements ContributionStrategy {

    private static final BigDecimal POOL_FACTOR_DIVISOR = new BigDecimal(
        "1000"
    );
    private static final BigDecimal POOL_PERCENTAGE_REDUCTION_FACTOR =
        new BigDecimal("0.005");
    private static final BigDecimal POOL_MINIMUM_CONTRIBUTION_PERCENTAGE =
        new BigDecimal("0.001");

    /**
     * Calculates a variable contribution amount where the rate changes based
     * on the jackpot's current size.
     * As per the requirements, the contribution is bigger when the jackpot is
     * small and decreases as the jackpot pool increases. This encourages the
     * jackpot to grow quickly from its initial value.
     *
     * To implement this, we basically try to reduce the base percentage by some
     * factor. Lets just divide the total jackpot pool by 1000, we can then round
     * this value to the nearest lower integer, multiply it by some reduction
     * factor which will give us a small enough value to subtract from the base
     * contribution percentage to the prize pool. Also, to ensure the contributions
     * don't turn zero set a minimum cap percentage.
     *
     * I think this replicates a dummy live betting prize pool scenario where things
     * like number of bets, pool size, and a lot of other things are considered.
     */
    @Override
    public BigDecimal calculateContribution(Bet bet, Jackpot jackpot) {
        BigDecimal basePercentage = jackpot.getContributionValue();
        BigDecimal poolFactor = jackpot
            .getCurrentPoolAmount()
            .divide(
                BigDecimal.valueOf(POOL_FACTOR_DIVISOR),
                0,
                RoundingMode.FLOOR
            );
        BigDecimal reduction = poolFactor.multiply(
            BigDecimal.valueOf(POOL_PERCENTAGE_REDUCTION_FACTOR)
        );
        BigDecimal currentPercentage = basePercentage.subtract(reduction);

        if (
            currentPercentage.compareTo(
                BigDecimal.valueOf(POOL_MINIMUM_CONTRIBUTION_PERCENTAGE)
            ) <
            0
        ) {
            currentPercentage = BigDecimal.valueOf(
                POOL_MINIMUM_CONTRIBUTION_PERCENTAGE
            );
        }
        return bet
            .getBetAmount()
            .multiply(currentPercentage)
            .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getStrategyName() {
        return "VARIABLE_PERCENTAGE_CONTRIBUTION";
    }
}
