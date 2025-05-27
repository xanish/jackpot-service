package io.github.xanish.jackpot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {

    private long timestamp;
    private int status;
    private String message;
    private String path;
}
