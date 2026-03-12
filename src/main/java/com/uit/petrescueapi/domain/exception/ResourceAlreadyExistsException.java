package com.uit.petrescueapi.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when attempting to create a resource that already exists
 * (duplicate email, username, etc.).
 */
public class ResourceAlreadyExistsException extends BaseException {

    public ResourceAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, "RESOURCE_ALREADY_EXISTS");
    }

    public ResourceAlreadyExistsException(String resource, String field, Object value) {
        super(String.format("%s already exists with %s: '%s'", resource, field, value),
                HttpStatus.CONFLICT, "RESOURCE_ALREADY_EXISTS");
    }
}
