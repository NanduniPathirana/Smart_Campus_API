package com.smartcampus.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps RoomNotEmptyException to HTTP 409 Conflict.
 * Triggered when a client tries to delete a room that still has sensors.
 * @Provider annotation registers this mapper automatically with Jersey.
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {

        // Build a clean JSON error body instead of exposing a stack trace
        Map<String, String> error = new HashMap<>();
        error.put("error", "Room Not Empty");
        error.put("message", ex.getMessage());

        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}