package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages all Sensor related endpoints under /api/v1/sensors.
 * Supports GET all with optional type filter, GET by ID and POST create.
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    /**
     * GET /api/v1/sensors
     * Returns all sensors. Optionally filter by type using ?type=CO2
     * Filtering is case-insensitive for better usability.
     */
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> list = new ArrayList<>(DataStore.sensors.values());

        // Apply case-insensitive filter if type query parameter is provided
        if (type != null && !type.isEmpty()) {
            list.removeIf(s -> !s.getType().equalsIgnoreCase(type));
        }
        return Response.ok(list).build();
    }

    /**
     * POST /api/v1/sensors
     * Registers a new sensor. Validates that the referenced roomId exists.
     * Returns 201 Created with Location header on success.
     */
    @POST
    public Response createSensor(Sensor sensor) {
        // Validate sensor ID is provided
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorBody("Sensor ID is required")).build();
        }
        // Validate that the referenced room exists - throws custom exception if not
        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "Room with ID '" + sensor.getRoomId() + "' does not exist.");
        }
        // Store the new sensor
        DataStore.sensors.put(sensor.getId(), sensor);

        // Add sensor ID to the room's sensor list
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());

        // Initialize an empty readings list for this sensor
        DataStore.readings.put(sensor.getId(), new CopyOnWriteArrayList<>());

        return Response.status(Response.Status.CREATED)
                .header("Location", "/api/v1/sensors/" + sensor.getId())
                .entity(sensor).build();
    }

    /**
     * GET /api/v1/sensors/{sensorId}
     * Returns detailed information for a specific sensor.
     */
    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        // Return 404 if sensor not found
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorBody("Sensor not found: " + sensorId)).build();
        }
        return Response.ok(sensor).build();
    }

    /**
     * Sub-resource locator for /api/v1/sensors/{sensorId}/readings
     * Delegates to SensorReadingResource for all reading operations.
     */
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

    // Helper method to build a consistent error response body
    private Map<String, String> errorBody(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}