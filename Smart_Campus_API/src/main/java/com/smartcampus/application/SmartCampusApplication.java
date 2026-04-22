package com.smartcampus.application;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JAX-RS Application entry point.
 * Sets the base URI for all API endpoints to /api/v1.
 * Jersey will auto-scan all packages for resources, filters and mappers.
 */
@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {
    // Jersey auto-scans and registers all components via web.xml config
}