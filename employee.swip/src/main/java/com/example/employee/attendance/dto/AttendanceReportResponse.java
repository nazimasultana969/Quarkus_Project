package com.example.employee.attendance.dto;

public record AttendanceReportResponse(

        Long employeeId,

        String employeeName,

        long presentDays,

        long lateDays,

        long halfDays,

        double totalHours
) {
}
