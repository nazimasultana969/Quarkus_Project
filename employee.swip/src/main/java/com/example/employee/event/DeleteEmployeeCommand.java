package com.example.employee.event;

import java.util.UUID;

public record DeleteEmployeeCommand(

        UUID sagaId,

        Long employeeId

) {
}