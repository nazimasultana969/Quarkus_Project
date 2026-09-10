package com.example.employee.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employee.orchestrator.EmployeeSagaOrchestrator;

@RestController
@RequestMapping("/api/saga")
public class SagaController {

	private final EmployeeSagaOrchestrator orchestrator;

	public SagaController(EmployeeSagaOrchestrator orchestrator) {

		this.orchestrator = orchestrator;
	}

	@PostMapping("/employee")
	public ResponseEntity<UUID> createEmployee(@RequestParam Long employeeId, @RequestParam String name,
			@RequestParam String email) {

		UUID sagaId = orchestrator.startSaga(employeeId, name, email);

		return ResponseEntity.ok(sagaId);
	}
}
