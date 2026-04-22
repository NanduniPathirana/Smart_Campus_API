package com.smartcampus.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps SensorUnavailableException to HTTP 403 Forbidden.
 * Triggered when a client tries to post a reading to a MAINTENANCE sensor.
 * @Provider annotation registers this mapper automatically with Jersey.
 */
@Provider
public class SensorUnavailableExceptionMapper 
        implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {

        // Build a clean JSON error body instead of exposing a stack trace
        Map<String, String> error = new HashMap<>();
        error.put("error", "Sensor Unavailable");
        error.put("message", ex.getMessage());

        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}