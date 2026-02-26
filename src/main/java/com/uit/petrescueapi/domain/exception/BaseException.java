package com.uit.petrescueapi.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base exception for all domain / business errors.
 * Carries an HTTP status and machine-readable error code so the
 * presentation layer can build a consistent API response.
 */
@Getter
public abstract class BaseException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    protected BaseException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    protected BaseException(String message, HttpStatus status, String errorCode, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }
}
