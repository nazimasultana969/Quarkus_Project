package com.example.employee.client;

import com.example.employee.dto.DepartmentResponse;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/departments")
@RegisterRestClient(configKey = "department-api")
@Produces(MediaType.APPLICATION_JSON)
public interface DepartmentClient {

    @GET
    @Path("/{id}/summary")
    DepartmentResponse getDepartment(@PathParam("id") Long id);
}