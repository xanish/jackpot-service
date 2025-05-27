package io.github.xanish.jackpot.strategies.rewards;

import io.github.xanish.jackpot.models.Bet;
import io.github.xanish.jackpot.models.Jackpot;
import java.math.BigDecimal;

public interface RewardStrategy {
    boolean shouldReward(Bet bet, Jackpot jackpot);
    BigDecimal getRewardAmount(Jackpot jackpot);
    String getStrategyName();
}
