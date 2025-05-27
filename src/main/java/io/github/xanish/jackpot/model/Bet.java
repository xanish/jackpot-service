package io.github.xanish.jackpot.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bet {

    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal betAmount;
}
