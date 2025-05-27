package io.github.xanish.jackpot.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BetRequestDto {

    @NotBlank(message = "Bet ID cannot be blank")
    @Size(
        min = 1,
        max = 50,
        message = "Bet ID must be between 1 and 50 characters"
    )
    private String betId;

    @NotBlank(message = "User ID cannot be blank")
    @Size(
        min = 1,
        max = 50,
        message = "User ID must be between 1 and 50 characters"
    )
    private String userId;

    @NotBlank(message = "Jackpot ID cannot be blank")
    @Size(
        min = 1,
        max = 50,
        message = "Jackpot ID must be between 1 and 50 characters"
    )
    private String jackpotId;

    @NotNull(message = "Bet amount cannot be null")
    @DecimalMin(
        value = "0.01",
        message = "Bet amount must be greater than or equal to 0.01"
    )
    private BigDecimal betAmount;
}
