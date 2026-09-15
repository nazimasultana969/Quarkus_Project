package com.example.department.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DepartmentExceptionMapper
        implements ExceptionMapper<DepartmentNotFoundException> {

    @Override
    public Response toResponse(
            DepartmentNotFoundException exception) {

        ErrorResponse response =
                new ErrorResponse(
                        exception.getMessage(),
                        404);

        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(response)
                .build();
    }
}