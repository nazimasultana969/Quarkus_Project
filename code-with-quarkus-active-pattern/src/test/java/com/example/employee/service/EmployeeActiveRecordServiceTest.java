package com.example.employee.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.employee.entity.EmployeeActiveRecord;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class EmployeeActiveRecordServiceTest {

	@Inject
	EmployeeActiveRecordService employeeService;

	@Test
	@Transactional
	public void testCreateEmployee() {

		EmployeeActiveRecord employee = new EmployeeActiveRecord();
		employee.name = "John";
		employee.email = "john@test.com";
		employee.department = "IT";

		EmployeeActiveRecord created = employeeService.createEmployee(employee);

		assertNotNull(created);
		assertNotNull(created.id);
		assertEquals("John", created.name);
		assertEquals("john@test.com", created.email);
		assertEquals("IT", created.department);
	}

	@Test
	@Transactional
	public void testGetEmployeeById() {

		EmployeeActiveRecord employee = new EmployeeActiveRecord();
		employee.name = "Alice";
		employee.email = "alice@test.com";
		employee.department = "HR";

		EmployeeActiveRecord created = employeeService.createEmployee(employee);

		EmployeeActiveRecord result = employeeService.getEmployeeById(created.id);

		assertNotNull(result);
		assertEquals(created.id, result.id);
		assertEquals("Alice", result.name);
		assertEquals("alice@test.com", result.email);
	}

	@Test
	@Transactional
	public void testGetAllEmployees() {

		EmployeeActiveRecord employee = new EmployeeActiveRecord();
		employee.name = "David";
		employee.email = "david@test.com";
		employee.department = "Finance";

		employeeService.createEmployee(employee);

		List<EmployeeActiveRecord> employees = employeeService.getAllEmployees();

		assertNotNull(employees);
		assertFalse(employees.isEmpty());
	}

	@Test
	@Transactional
	public void testUpdateEmployee() {

		EmployeeActiveRecord employee = new EmployeeActiveRecord();
		employee.name = "Robert";
		employee.email = "robert@test.com";
		employee.department = "IT";

		EmployeeActiveRecord created = employeeService.createEmployee(employee);

		employeeService.updateEmployee((long) 8766, created);

		EmployeeActiveRecord updated = employeeService.getEmployeeById(created.id);

		assertEquals("Robert Updated", updated.name);
		assertEquals("robert.updated@test.com", updated.email);
		assertEquals("HR", updated.department);
	}

	@Test
	@Transactional
	public void testDeleteEmployee() {

		EmployeeActiveRecord employee = new EmployeeActiveRecord();
		employee.name = "Delete User";
		employee.email = "delete@test.com";
		employee.department = "IT";

		EmployeeActiveRecord created = employeeService.createEmployee(employee);

		Long id = created.id;

		employeeService.deleteEmployee(id);

		EmployeeActiveRecord result = EmployeeActiveRecord.findEmployeeById(id);

		assertNull(result);
	}
}