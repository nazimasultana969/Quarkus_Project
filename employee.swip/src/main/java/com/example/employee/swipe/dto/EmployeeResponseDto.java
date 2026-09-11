package com.example.employee.swipe.dto;

public record EmployeeResponseDto(
        Long id,
        String name,
        String email,
        Double salary) {
}