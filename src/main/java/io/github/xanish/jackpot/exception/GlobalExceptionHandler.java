package io.github.xanish.jackpot.exception;

import io.github.xanish.jackpot.dto.ErrorResponseDto;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(
        GlobalExceptionHandler.class
    );

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        Map<String, String> errors = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(
                Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage
                )
            );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", status.value());
        body.put("errors", errors);
        body.put("message", "Validation failed");

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(JackpotNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleJackpotNotFoundException(
        JackpotNotFoundException ex,
        WebRequest request
    ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            System.currentTimeMillis(),
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StrategyNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleStrategyNotFoundException(
        StrategyNotFoundException ex,
        WebRequest request
    ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            System.currentTimeMillis(),
            HttpStatus.NOT_IMPLEMENTED.value(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllUncaughtException(
        Exception ex,
        WebRequest request
    ) {
        // Extract request string without any sensitive details like session id / ip address
        String path = request.getDescription(false).replace("uri=", "");

        logger.error(
            "Unexpected error on path [{}]: {}",
            path,
            ex.getMessage(),
            ex
        );

        ErrorResponseDto errorResponse = new ErrorResponseDto(
            System.currentTimeMillis(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected internal server error occurred. Please contact support.",
            path
        );

        return new ResponseEntity<>(
            errorResponse,
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
