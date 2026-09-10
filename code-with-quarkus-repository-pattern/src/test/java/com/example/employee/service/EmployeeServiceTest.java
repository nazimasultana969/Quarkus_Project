package com.example.employee.service;

import com.example.employee.entity.Employee;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

	@Mock
	EmployeeRepository employeeRepository;

	@InjectMocks
	EmployeeService employeeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void shouldGetAllEmployees() {

		Employee employee = new Employee("John", "john@test.com", "IT");

		when(employeeRepository.listAll()).thenReturn(List.of(employee));

		List<Employee> result = employeeService.getAllEmployees();

		assertEquals(1, result.size());
		assertEquals("John", result.get(0).getName());

		verify(employeeRepository).listAll();
	}

	@Test
	void shouldGetEmployeeById() {

		Employee employee = new Employee("John", "john@test.com", "IT");

		employee.setId(1L);

		when(employeeRepository.findById(1L)).thenReturn(employee);

		Employee result = employeeService.getEmployeeById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("John", result.getName());

		verify(employeeRepository).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenEmployeeNotFound() {

		when(employeeRepository.findById(100L)).thenReturn(null);

		assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(100L));

		verify(employeeRepository).findById(100L);
	}

	@Test
	void shouldCreateEmployee() {

		Employee employee = new Employee("John", "john@test.com", "IT");

		Employee result = employeeService.createEmployee(employee);

		assertNotNull(result);
		assertEquals("John", result.getName());

		verify(employeeRepository).persist(employee);
	}

	@Test
	void shouldUpdateEmployee() {

		Employee existing = new Employee("John", "john@test.com", "IT");

		existing.setId(1L);

		Employee update = new Employee("John Updated", "john.updated@test.com", "HR");

		when(employeeRepository.findById(1L)).thenReturn(existing);

		Employee result = employeeService.updateEmployee(1L, update);

		assertEquals("John Updated", result.getName());
		assertEquals("john.updated@test.com", result.getEmail());
		assertEquals("HR", result.getDepartment());

		verify(employeeRepository).findById(1L);
	}

	@Test
	void shouldDeleteEmployee() {

		when(employeeRepository.deleteById(1L)).thenReturn(true);

		employeeService.deleteEmployee(1L);

		verify(employeeRepository).deleteById(1L);
	}

	@Test
	void shouldThrowExceptionWhenDeleteEmployeeNotFound() {

		when(employeeRepository.deleteById(100L)).thenReturn(false);

		assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(100L));

		verify(employeeRepository).deleteById(100L);
	}
}