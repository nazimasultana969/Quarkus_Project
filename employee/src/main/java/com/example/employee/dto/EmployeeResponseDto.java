package com.example.employee.dto;

public record EmployeeResponseDto(
        Long id,
        String name,
        String email,
        Double salary
) {
}