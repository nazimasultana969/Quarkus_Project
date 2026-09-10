package com.example.employee.service;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

	EmployeeResponseDto saveEmployee(EmployeeRequestDto dto);

	EmployeeResponseDto getEmployee(Long id);

	Page<EmployeeResponseDto> getAllEmployees(Pageable pageable);

	EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto dto);

	void deleteEmployee(Long id);
}