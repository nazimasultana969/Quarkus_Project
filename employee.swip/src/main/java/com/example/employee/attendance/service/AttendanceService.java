package com.example.employee.attendance.service;

import com.example.employee.attendance.dto.AttendanceReportResponse;
import com.example.employee.attendance.dto.AttendanceResponse;

public interface AttendanceService {

	AttendanceResponse checkIn(Long employeeId);

	AttendanceResponse checkOut(Long employeeId);

	AttendanceReportResponse generateReport(Long employeeId);
}