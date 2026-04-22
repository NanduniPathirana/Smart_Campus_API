package com.smartcampus.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps LinkedResourceNotFoundException to HTTP 422 Unprocessable Entity.
 * Triggered when a client tries to create a sensor with a roomId that does not exist.
 * @Provider annotation registers this mapper automatically with Jersey.
 */
@Provider
public class LinkedResourceNotFoundExceptionMapper 
        implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {

        // Build a clean JSON error body instead of exposing a stack trace
        Map<String, String> error = new HashMap<>();
        error.put("error", "Linked Resource Not Found");
        error.put("message", ex.getMessage());

        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}