package io.github.xanish.jackpot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RewardEvaluationRequestDto {

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
}
