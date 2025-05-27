package io.github.xanish.jackpot.models;

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
