package com.example.employee.event;

import java.util.UUID;

public record EmployeeCreatedResult(

        UUID sagaId,

        Long employeeId,

        String name,

        String email,

        boolean success,

        String message

) {
}