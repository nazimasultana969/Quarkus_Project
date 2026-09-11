package com.example.employee.swipe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.swipe.dto.SwipeResponse;
import com.example.employee.swipe.service.EmployeeSwipeService;

@RestController
@RequestMapping("/swipes")
public class EmployeeSwipeController {

	private final EmployeeSwipeService service;

	public EmployeeSwipeController(EmployeeSwipeService service) {

		this.service = service;
	}

	@PostMapping("/{employeeId}/checkin")
	public ResponseEntity<SwipeResponse> checkIn(@PathVariable Long employeeId) {

		return ResponseEntity.ok(service.checkIn(employeeId));
	}

	@PostMapping("/{employeeId}/checkout")
	public ResponseEntity<SwipeResponse> checkOut(@PathVariable Long employeeId) {

		return ResponseEntity.ok(service.checkOut(employeeId));
	}
}