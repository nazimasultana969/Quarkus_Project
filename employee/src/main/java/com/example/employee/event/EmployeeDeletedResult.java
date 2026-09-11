package com.example.employee.event;

import java.util.UUID;

public record EmployeeDeletedResult(

        UUID sagaId,

        Long employeeId,

        boolean success,

        String message

) {
}