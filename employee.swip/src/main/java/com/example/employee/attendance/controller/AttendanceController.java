package com.example.employee.attendance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.attendance.dto.AttendanceReportResponse;
import com.example.employee.attendance.dto.AttendanceResponse;
import com.example.employee.attendance.service.AttendanceService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	private final AttendanceService service;

	public AttendanceController(AttendanceService service) {

		this.service = service;
	}

	@PostMapping("/{employeeId}/checkin")
	public ResponseEntity<AttendanceResponse> checkIn(@PathVariable Long employeeId) {

		return ResponseEntity.ok(service.checkIn(employeeId));
	}

	@PostMapping("/{employeeId}/checkout")
	public ResponseEntity<AttendanceResponse> checkOut(@PathVariable Long employeeId) {

		return ResponseEntity.ok(service.checkOut(employeeId));
	}

	@GetMapping("/{employeeId}/report")
	public ResponseEntity<AttendanceReportResponse> getReport(@PathVariable Long employeeId) {

		return ResponseEntity.ok(service.generateReport(employeeId));
	}
}