package com.uit.petrescueapi.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when the caller lacks the required role or permission.
 */
public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN, "FORBIDDEN");
    }
}
