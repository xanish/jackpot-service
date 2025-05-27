package io.github.xanish.jackpot.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bet {
    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal betAmount;
}
