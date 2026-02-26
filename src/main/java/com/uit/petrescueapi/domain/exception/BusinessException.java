package com.uit.petrescueapi.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a business rule is violated.
 */
public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "BUSINESS_ERROR");
    }

    public BusinessException(String message, String errorCode) {
        super(message, HttpStatus.BAD_REQUEST, errorCode);
    }
}
