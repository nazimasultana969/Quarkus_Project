package com.example.employee.dto;

import com.example.employee.entity.Employee;

import java.util.List;

public record EmployeePageResponse(

		List<Employee> employees, long totalElements, int totalPages, int currentPage, int pageSize) {
}