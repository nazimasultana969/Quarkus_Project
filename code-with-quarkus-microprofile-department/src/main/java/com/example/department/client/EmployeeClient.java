package com.example.department.client;

import com.example.department.dto.EmployeeResponse;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/employees")
@RegisterRestClient(configKey = "employee-api")
@Produces(MediaType.APPLICATION_JSON)
public interface EmployeeClient {

    @GET
    @Path("/{id}/summary")
    EmployeeResponse getEmployee(
            @PathParam("id") Long id);
}