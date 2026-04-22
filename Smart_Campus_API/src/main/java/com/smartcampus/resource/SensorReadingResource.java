package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

// Sub-resource class handling sensor reading operations.
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    // The sensor ID passed from the parent SensorResource
    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * GET /api/v1/sensors/{sensorId}/readings
     * Returns the full historical reading list for a specific sensor.
     */
    @GET
    public Response getReadings() {
        Sensor sensor = DataStore.sensors.get(sensorId);
        // Return 404 if sensor not found
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorBody("Sensor not found: " + sensorId)).build();
        }
        // Get readings list or return empty list if none exist
        List<SensorReading> history = DataStore.readings.getOrDefault(
                sensorId, new CopyOnWriteArrayList<>());
        return Response.ok(history).build();
    }

    /**
     * POST /api/v1/sensors/{sensorId}/readings
     * Appends a new reading for the sensor.
     * Side effect: updates the parent sensor's currentValue field.
     * Blocked if sensor status is MAINTENANCE.
     */
    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        // Return 404 if sensor not found
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorBody("Sensor not found: " + sensorId)).build();
        }
        // Block readings if sensor is under maintenance - throws custom exception
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                "Sensor " + sensorId + " is under maintenance and cannot accept readings.");
        }
        // Auto generate unique ID and timestamp for the reading
        reading.setId(UUID.randomUUID().toString());
        reading.setTimestamp(System.currentTimeMillis());

        // Add reading to the sensor's history
        DataStore.readings.computeIfAbsent(
                sensorId, k -> new CopyOnWriteArrayList<>()).add(reading);

        // Side effect: update parent sensor's currentValue for data consistency
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }

    // Helper method to build a consistent error response body
    private Map<String, String> errorBody(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}