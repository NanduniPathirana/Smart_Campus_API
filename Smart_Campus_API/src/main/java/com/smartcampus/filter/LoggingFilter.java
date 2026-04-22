package com.smartcampus.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Logging filter for API observability.
 * Implements both ContainerRequestFilter and ContainerResponseFilter
 * to log every incoming request and every outgoing response.
 * @Provider registers this filter automatically with Jersey.
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    // Single logger instance for this filter class
    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    /**
     * Logs the HTTP method and URI of every incoming request.
     * Runs before the request reaches any resource method.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.info("Incoming Request: ["
                + requestContext.getMethod()
                + "] "
                + requestContext.getUriInfo().getRequestUri());
    }

    /**
     * Logs the HTTP status code of every outgoing response.
     * Runs after the resource method has returned a response.
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        LOGGER.info("Outgoing Response: Status ["
                + responseContext.getStatus()
                + "] for ["
                + requestContext.getMethod()
                + "] "
                + requestContext.getUriInfo().getRequestUri());
    }
}