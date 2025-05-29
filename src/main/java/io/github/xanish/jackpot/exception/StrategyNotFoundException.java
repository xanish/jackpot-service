package io.github.xanish.jackpot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a requested contribution or reward strategy is not implemented or found.
 */
@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
public class StrategyNotFoundException extends RuntimeException {

    public StrategyNotFoundException(String message) {
        super(message);
    }
}
