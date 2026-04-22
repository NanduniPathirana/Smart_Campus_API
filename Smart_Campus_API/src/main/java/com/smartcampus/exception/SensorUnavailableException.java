package com.smartcampus.exception;

/**
 * Thrown when a client attempts to post a reading
 * to a sensor that is currently under MAINTENANCE.
 * Mapped to HTTP 403 Forbidden by SensorUnavailableExceptionMapper.
 */
public class SensorUnavailableException extends RuntimeException {

    public SensorUnavailableException(String message) {
        super(message);
    }
}