package com.example.employee.service;

import com.example.employee.entity.EmployeeActiveRecord;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class EmployeeActiveRecordService {

	public List<EmployeeActiveRecord> getAllEmployees() {

		return EmployeeActiveRecord.findAllEmployees();
	}

	public EmployeeActiveRecord getEmployeeById(Long id) {

		EmployeeActiveRecord employee = EmployeeActiveRecord.findEmployeeById(id);

		if (employee == null) {
			throw new RuntimeException("Employee not found with id: " + id);
		}

		return employee;
	}

	@Transactional
	public EmployeeActiveRecord createEmployee(EmployeeActiveRecord employee) {

		EmployeeActiveRecord.createEmployee(employee);

		return employee;
	}

	@Transactional
	public EmployeeActiveRecord updateEmployee(Long id, EmployeeActiveRecord employee) {

		EmployeeActiveRecord existing = getEmployeeById(id);

		existing.updateEmployee(employee.name, employee.email, employee.department);

		return existing;
	}

	@Transactional
	public void deleteEmployee(Long id) {

		EmployeeActiveRecord employee = getEmployeeById(id);

		employee.deleteEmployee();
	}
}