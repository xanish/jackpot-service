package io.github.xanish.jackpot.strategies.contributions;

package io.github.xanish.jackpot.models.Bet;
package io.github.xanish.jackpot.models.Jackpot;

import java.math.BigDecimal;

public interface ContributionStrategy {
    BigDecimal calculateContribution(Bet bet, Jackpot jackpot);
    String getStrategyName();
}
