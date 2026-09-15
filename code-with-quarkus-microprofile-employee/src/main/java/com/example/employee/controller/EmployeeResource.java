package com.example.employee.controller;

import com.example.employee.client.DepartmentClient;
import com.example.employee.dto.DepartmentResponse;
import com.example.employee.dto.EmployeeResponse;
import com.example.employee.service.EmployeeService;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/employees")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    private static final Logger LOG =
            Logger.getLogger(EmployeeResource.class);

    @Inject
    EmployeeService employeeService;

    @Inject
    @RestClient
    DepartmentClient departmentClient;

    @GET
    @Path("/{id}")
    public EmployeeResponse getEmployee(
            @PathParam("id") Long id) {

        LOG.infof("GET employee request: %d", id);

        return employeeService.getEmployee(id);
    }

    @GET
    @Path("/{id}/department")
    public DepartmentResponse getEmployeeDepartment(
            @PathParam("id") Long id) {

        LOG.infof(
                "Calling Department Service for employee: %d",
                id
        );

        EmployeeResponse employee =
                employeeService.getEmployee(id);

        return departmentClient.getDepartment(
                employee.departmentId()
        );
    }

    @GET
    @Path("/{id}/summary")
    public EmployeeResponse getEmployeeSummary(
            @PathParam("id") Long id) {

        LOG.infof(
                "Employee summary request: %d",
                id
        );

        return employeeService.getEmployeeSummary(id);
    }
}