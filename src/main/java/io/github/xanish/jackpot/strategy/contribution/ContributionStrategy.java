package io.github.xanish.jackpot.strategy.contribution;

import io.github.xanish.jackpot.model.Bet;
import io.github.xanish.jackpot.model.Jackpot;
import java.math.BigDecimal;

public interface ContributionStrategy {
    BigDecimal calculateContribution(Bet bet, Jackpot jackpot);
    String getStrategyName();
}
