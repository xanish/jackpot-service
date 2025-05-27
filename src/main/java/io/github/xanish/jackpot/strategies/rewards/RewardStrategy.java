package io.github.xanish.jackpot.strategies.rewards;

import io.github.xanish.jackpot.model.Bet;
import io.github.xanish.jackpot.model.Jackpot;
import java.math.BigDecimal;

public interface RewardStrategy {
    boolean shouldReward(Bet bet, Jackpot jackpot);
    BigDecimal getRewardAmount(Jackpot jackpot);
    String getStrategyName();
}
