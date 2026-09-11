package com.example.employee.event;

import java.util.UUID;

public record CreateEmployeeCommand(

		UUID sagaId,

		Long employeeId,

		String name,

		String email

) {
}