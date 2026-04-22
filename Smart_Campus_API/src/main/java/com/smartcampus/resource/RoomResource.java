package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all Room related endpoints under /api/v1/rooms.
 * Supports GET all, GET by ID, POST create and DELETE operations.
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    /**
     * GET /api/v1/rooms
     * Returns a list of all rooms in the system.
     */
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(roomList).build();
    }

    /**
     * POST /api/v1/rooms
     * Creates a new room. Returns 201 Created with Location header on success.
     */
    @POST
    public Response createRoom(Room room) {
        // Validate room ID is provided
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorBody("Room ID is required")).build();
        }
        // Check for duplicate room ID
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(errorBody("Room with ID " + room.getId() + " already exists")).build();
        }
        // Store the new room
        DataStore.rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED)
                .header("Location", "/api/v1/rooms/" + room.getId())
                .entity(room).build();
    }

    /**
     * GET /api/v1/rooms/{roomId}
     * Returns detailed metadata for a specific room.
     */
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        // Return 404 if room not found
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorBody("Room not found: " + roomId)).build();
        }
        return Response.ok(room).build();
    }

    /**
     * DELETE /api/v1/rooms/{roomId}
     * Deletes a room. Blocked if room still has sensors assigned.
     */
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        // Return 404 if room not found
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorBody("Room not found: " + roomId)).build();
        }
        // Block deletion if room still has sensors - throws custom exception
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                "Room " + roomId + " still has active sensors assigned. Remove them first.");
        }
        // Remove the room from the data store
        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }

    // Helper method to build a consistent error response body
    private Map<String, String> errorBody(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}