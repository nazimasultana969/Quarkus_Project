package com.example.employee.service;

import java.util.List;

import com.example.employee.dto.EmployeePageResponse;
import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EmployeeService {

	@Inject
	EmployeeRepository employeeRepository;

	public List<Employee> getAllEmployees() {
		return employeeRepository.listAll();
	}

	public Employee getEmployeeById(Long id) {

		Employee employee = employeeRepository.findById(id);

		if (employee == null) {

			throw new RuntimeException("Employee not found with id: " + id);
		}

		return employee;
	}

	@Transactional
	public Employee createEmployee(Employee employee) {

		employeeRepository.persist(employee);

		return employee;
	}

	@Transactional
	public Employee updateEmployee(Long id, Employee employee) {

		Employee existingEmployee = getEmployeeById(id);

		existingEmployee.setName(employee.getName());
		existingEmployee.setEmail(employee.getEmail());
		existingEmployee.setDepartment(employee.getDepartment());

		return existingEmployee;
	}

	@Transactional
	public void deleteEmployee(Long id) {

		boolean deleted = employeeRepository.deleteById(id);

		if (!deleted) {

			throw new RuntimeException("Employee not found with id: " + id);
		}
	}

	// Pagination + Filter
	public EmployeePageResponse getEmployees(String name, String email, String department, int page, int size) {

		if (page < 0) {
			throw new IllegalArgumentException("Page cannot be negative");
		}

		if (size <= 0) {
			throw new IllegalArgumentException("Size must be greater than zero");
		}

		if (size > 100) {
			throw new IllegalArgumentException("Size cannot be greater than 100");
		}

		List<Employee> employees = employeeRepository.findEmployees(name, email, department, page, size);

		long totalElements = employeeRepository.countEmployees(name, email, department);

		int totalPages = (int) Math.ceil((double) totalElements / size);

		return new EmployeePageResponse(employees, totalElements, totalPages, page, size);
	}
}