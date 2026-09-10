package com.example.employee.event;

public record EmployeeCreatedEvent(

        Long employeeId,
        String name,
        String email

) {
}