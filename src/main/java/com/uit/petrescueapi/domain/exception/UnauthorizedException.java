package com.uit.petrescueapi.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when authentication fails (bad credentials, expired token, etc.).
 */
public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
}
