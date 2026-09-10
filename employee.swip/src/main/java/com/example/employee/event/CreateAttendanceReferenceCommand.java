package com.example.employee.event;

import java.util.UUID;

public record CreateAttendanceReferenceCommand(

        UUID sagaId,

        Long employeeId,

        String name,

        String email

) {
}
