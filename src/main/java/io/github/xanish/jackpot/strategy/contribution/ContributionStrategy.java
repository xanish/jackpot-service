package io.github.xanish.jackpot.strategy.contribution;

package io.github.xanish.jackpot.model.Bet;
package io.github.xanish.jackpot.model.Jackpot;

import java.math.BigDecimal;

public interface ContributionStrategy {
    BigDecimal calculateContribution(Bet bet, Jackpot jackpot);
    String getStrategyName();
}
