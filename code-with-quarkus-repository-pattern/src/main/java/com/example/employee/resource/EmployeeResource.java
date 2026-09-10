package com.example.employee.resource;

import com.example.employee.dto.EmployeePageResponse;
import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {

	@Inject
	EmployeeService employeeService;

	@GET
	public List<Employee> getAllEmployees() {

		return employeeService.getAllEmployees();
	}

	@GET
	@Path("/search")
	public EmployeePageResponse searchEmployees(
			@QueryParam("name") String name,
			@QueryParam("email") String email,
			@QueryParam("department") String department,
			@QueryParam("page") @DefaultValue("0") int page,
			@QueryParam("size") @DefaultValue("10") int size) {

		return employeeService.getEmployees(name, email, department, page, size);
	}

	@GET
	@Path("/{id}")
	public Employee getEmployeeById(@PathParam("id") Long id) {

		return employeeService.getEmployeeById(id);
	}

	@POST
	public Response createEmployee(Employee employee) {

		Employee createdEmployee = employeeService.createEmployee(employee);

		return Response.status(Response.Status.CREATED).entity(createdEmployee).build();
	}

	@PUT
	@Path("/{id}")
	public Employee updateEmployee(@PathParam("id") Long id, Employee employee) {

		return employeeService.updateEmployee(id, employee);
	}

	@DELETE
	@Path("/{id}")
	public Response deleteEmployee(@PathParam("id") Long id) {

		employeeService.deleteEmployee(id);

		return Response.noContent().build();
	}
}