package com.example.employee.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

	private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

	@Override
	public Response toResponse(Exception exception) {

		LOG.error("Exception occurred", exception);

		Map<String, String> response = new HashMap<>();

		response.put("message", exception.getMessage());

		int status = 500;

		if (exception instanceof EmployeeNotFoundException) {
			status = 404;
		}

		return Response.status(status).entity(response).type(MediaType.APPLICATION_JSON).build();
	}
}