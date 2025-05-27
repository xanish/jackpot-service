package io.github.xanish.jackpot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BetAcceptedResponseDto {

    private long timestamp;
    private int status;
    private String message;
}
