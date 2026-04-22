package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Discovery endpoint - provides API metadata and available resource links.
 * Implements HATEOAS by returning navigable links to all primary resources.
 */
@Path("/")
public class DiscoveryResource {

    /**
     * GET /api/v1
     * Returns API metadata including version, contact and resource links.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {

        // Basic API information
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Smart Campus API");
        info.put("version", "1.0");
        info.put("description", "RESTful API for Smart Campus Sensor and Room Management");
        info.put("contact", "admin@smartcampus.ac.uk");

        // HATEOAS - links to primary resource collections
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        info.put("resources", links);

        return Response.ok(info).build();
    }
}