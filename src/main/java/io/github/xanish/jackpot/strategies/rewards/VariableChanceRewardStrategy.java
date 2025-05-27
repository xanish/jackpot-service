package io.github.xanish.jackpot.strategies.rewards;

import io.github.xanish.jackpot.models.Bet;
import io.github.xanish.jackpot.models.Jackpot;
import java.math.BigDecimal;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component("VARIABLE_CHANCE_REWARD")
public class VariableChanceRewardStrategy implements RewardStrategy {

    private static final BigDecimal DEFAULT_REWARD_POOL_LIMIT = new BigDecimal(
        "100000"
    );
    private static final BigDecimal MAXIMUM_BONUS_CHANCE_FACTOR =
        new BigDecimal("0.49");
    private static final BigDecimal INTERMEDIATE_CHANCE_CAP = new BigDecimal(
        "0.75"
    );

    private final Random random = new Random();

    /**
     * Determines the variable chance of winning the jackpot for a given bet.
     *
     * The formula here was designed specifically to meet two key requirements:
     * 1. The chance to win must start small and get bigger as the jackpot pool increases.
     * 2. If the pool reaches a pre-defined limit, the win chance must become 100%.
     *
     * To achieve this, the calculation was broken down with the following reasoning:
     * - A "guaranteed win" check is performed first, as it's the most critical rule.
     * - The variable chance is modeled as a simple, understandable formula:
     *   (current chance) = (a small base chance) + (a bonus chance).
     * - The 'bonus chance' is directly tied to the pool's size. We calculate
     *   how "full" the pool is (the `poolRatio`) and use that to scale the
     *   bonus. This ensures the chance grows proportionally with the pool.
     * - Constants like `MAXIMUM_BONUS_CHANCE_FACTOR` and `INTERMEDIATE_CHANCE_CAP`
     *   act as "tuning knobs." They give us control over the player experience,
     *   allowing us to adjust how quickly the win-chance increases and preventing
     *   it from getting too high too fast, which helps keep the jackpot exciting for longer.
     */
    @Override
    public boolean shouldReward(Bet bet, Jackpot jackpot) {
        if (
            jackpot.getRewardPoolLimit() != null &&
            jackpot
                .getCurrentPoolAmount()
                .compareTo(jackpot.getRewardPoolLimit()) >=
            0
        ) {
            return true;
        }

        BigDecimal initialChance = jackpot.getRewardValue();
        BigDecimal poolLimit = jackpot.getRewardPoolLimit() == null
            ? DEFAULT_REWARD_POOL_LIMIT
            : jackpot.getRewardPoolLimit();

        if (poolLimit.compareTo(BigDecimal.ZERO) == 0) {
            return random.nextDouble() < initialChance.doubleValue();
        }

        BigDecimal poolRatio = jackpot
            .getCurrentPoolAmount()
            .divide(poolLimit, 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal bonusChance = poolRatio.multiply(
            MAXIMUM_BONUS_CHANCE_FACTOR
        );
        BigDecimal currentChance = initialChance.add(bonusChance);

        if (currentChance.compareTo(INTERMEDIATE_CHANCE_CAP) > 0) {
            currentChance = INTERMEDIATE_CHANCE_CAP;
        }

        return random.nextDouble() < currentChance.doubleValue();
    }

    @Override
    public BigDecimal getRewardAmount(Jackpot jackpot) {
        return jackpot.getCurrentPoolAmount();
    }

    @Override
    public String getStrategyName() {
        return "VARIABLE_CHANCE_REWARD";
    }
}
