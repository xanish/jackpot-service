package io.github.xanish.jackpot.strategies.rewards;

import io.github.xanish.jackpot.model.Bet;
import io.github.xanish.jackpot.model.Jackpot;
import java.math.BigDecimal;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component("FIXED_CHANCE_REWARD")
public class FixedChanceRewardStrategy implements RewardStrategy {

    private final Random random = new Random();

    @Override
    public boolean shouldReward(Bet bet, Jackpot jackpot) {
        // Since jackpot.getRewardValue() is the % chance of winning in fixed
        // strategy we just need to check if a random number less than it can
        // be rolled or not
        double chance = jackpot.getRewardValue().doubleValue();
        return random.nextDouble() < chance;
    }

    @Override
    public BigDecimal getRewardAmount(Jackpot jackpot) {
        return jackpot.getCurrentPoolAmount();
    }

    @Override
    public String getStrategyName() {
        return "FIXED_CHANCE_REWARD";
    }
}
