package com.example.employee.mapper;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.entity.Employee;

public class EmployeeMapper {

    private EmployeeMapper() {
    }

    public static Employee toEntity(EmployeeRequestDto dto) {

        Employee employee = new Employee();

        employee.setName(dto.name());
        employee.setEmail(dto.email());
        employee.setSalary(dto.salary());

        return employee;
    }

    public static EmployeeResponseDto toDto(Employee employee) {

        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary()
        );
    }
}