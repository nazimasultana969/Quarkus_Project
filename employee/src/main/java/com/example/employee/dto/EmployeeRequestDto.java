package com.example.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record EmployeeRequestDto(

        @NotBlank(message = "Name cannot be empty")
        String name,

        @Email(message = "Invalid email format")
        String email,

        @Positive(message = "Salary must be greater than zero")
        Double salary

) {
}