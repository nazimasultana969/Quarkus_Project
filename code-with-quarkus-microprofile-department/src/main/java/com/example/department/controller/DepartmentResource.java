package com.example.department.controller;

import com.example.department.client.EmployeeClient;
import com.example.department.dto.DepartmentResponse;
import com.example.department.dto.EmployeeResponse;
import com.example.department.service.DepartmentService;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/departments")
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentResource {

    private static final Logger LOG =
            Logger.getLogger(DepartmentResource.class);

    @Inject
    DepartmentService departmentService;

    @Inject
    @RestClient
    EmployeeClient employeeClient;

    @GET
    @Path("/{id}")
    public DepartmentResponse getDepartment(
            @PathParam("id") Long id) {

        LOG.infof(
                "GET department request: %d",
                id
        );

        return departmentService.getDepartment(id);
    }

    @GET
    @Path("/{id}/employee")
    public EmployeeResponse getDepartmentEmployee(
            @PathParam("id") Long id) {

        LOG.infof(
                "Calling Employee Service for department: %d",
                id
        );

        departmentService.getDepartment(id);

        // Employee 1 belongs to department 100
        return employeeClient.getEmployee(1L);
    }

    @GET
    @Path("/{id}/summary")
    public DepartmentResponse getDepartmentSummary(
            @PathParam("id") Long id) {

        LOG.infof(
                "Department summary request: %d",
                id
        );

        return departmentService.getDepartment(id);
    }
}