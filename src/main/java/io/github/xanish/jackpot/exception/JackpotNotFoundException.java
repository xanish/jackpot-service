package io.github.xanish.jackpot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an operation is attempted on a Jackpot that cannot be found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class JackpotNotFoundException extends RuntimeException {

    public JackpotNotFoundException(String message) {
        super(message);
    }
}
