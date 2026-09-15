package com.example.employee.service;

import com.example.employee.dto.EmployeeResponse;
import com.example.employee.exception.EmployeeNotFoundException;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EmployeeService {

	private static final Logger LOG = Logger.getLogger(EmployeeService.class);

	public EmployeeResponse getEmployee(Long id) {

		LOG.infof("Getting employee with id: %d", id);

		if (id == 1) {
			return new EmployeeResponse(1L, "Nazima", 100L);
		}

		LOG.errorf("Employee not found: %d", id);

		throw new EmployeeNotFoundException("Employee not found with id: " + id);
	}

	public EmployeeResponse getEmployeeSummary(Long id) {

		LOG.infof("Getting employee summary for id: %d", id);

		return getEmployee(id);
	}
}