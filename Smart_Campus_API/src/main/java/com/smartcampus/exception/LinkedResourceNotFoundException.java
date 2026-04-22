package com.smartcampus.exception;

/**
 * Thrown when a client attempts to create a sensor
 * with a roomId that does not exist in the system.
 * Mapped to HTTP 422 Unprocessable Entity by LinkedResourceNotFoundExceptionMapper.
 */
public class LinkedResourceNotFoundException extends RuntimeException {

    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}