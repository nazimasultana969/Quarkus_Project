package com.example.employee.event;

import java.util.UUID;

public record AttendanceCreatedResult(

        UUID sagaId,

        Long employeeId,

        boolean success,

        String message

) {
}
