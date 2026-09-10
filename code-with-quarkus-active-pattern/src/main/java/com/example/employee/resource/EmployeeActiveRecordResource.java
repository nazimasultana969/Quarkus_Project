package com.example.employee.resource;

import com.example.employee.entity.EmployeeActiveRecord;
import com.example.employee.service.EmployeeActiveRecordService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/active-record/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeActiveRecordResource {

	@Inject
	EmployeeActiveRecordService service;
	
	@GET
	public List<EmployeeActiveRecord> getAllEmployees() {

		return EmployeeActiveRecord.findAllEmployees();
	}

	@GET
	@Path("/{id}")
	public EmployeeActiveRecord getEmployee(@PathParam("id") Long id) {

		return EmployeeActiveRecord.findEmployeeById(id);
	}

	@POST
	public Response createEmployee(EmployeeActiveRecord employee) {

		EmployeeActiveRecord created = service.createEmployee(employee);

		return Response.status(Response.Status.CREATED).entity(created).build();
	}

	@PUT
	@Path("/{id}")
	public EmployeeActiveRecord updateEmployee(@PathParam("id") Long id, EmployeeActiveRecord employee) {

		return service.updateEmployee(id, employee);
	}

	@DELETE
	@Path("/{id}")
	public Response deleteEmployee(@PathParam("id") Long id) {

		service.deleteEmployee(id);

		return Response.noContent().build();
	}
}