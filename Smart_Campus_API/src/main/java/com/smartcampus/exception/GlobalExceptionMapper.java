package com.smartcampus.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global catch-all exception mapper for any unexpected runtime errors.
 * Intercepts any Throwable not handled by other mappers.
 * Returns HTTP 500 Internal Server Error with a safe generic message.
 * This ensures no raw Java stack traces are ever exposed to API consumers.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    // Logger to record the full error details on the server side only
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable ex) {

        // Log the full stack trace on the server for debugging purposes
        LOGGER.log(Level.SEVERE, "Unexpected error occurred: ", ex);

        // Return a safe generic message to the client - never expose internal details
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", "An unexpected error occurred. Please contact the administrator.");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}