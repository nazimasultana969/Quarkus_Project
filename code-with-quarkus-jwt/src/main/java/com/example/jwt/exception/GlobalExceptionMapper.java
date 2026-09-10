package com.example.jwt.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<RuntimeException> {

	private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

	@Override
	public Response toResponse(RuntimeException exception) {

		LOG.error("Application exception", exception);

		return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("message", exception.getMessage())).build();
	}
}