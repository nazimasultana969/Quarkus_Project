package com.example.employee.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.dto.ApiResponse;
import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeService service;

	public EmployeeController(EmployeeService service) {

		this.service = service;
	}

	@PostMapping
	public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto dto) {

		return ResponseEntity.status(HttpStatus.CREATED).body(service.saveEmployee(dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeResponseDto> getEmployee(@PathVariable Long id) {

		return ResponseEntity.ok(service.getEmployee(id));
	}

	@GetMapping
	public ResponseEntity<Page<EmployeeResponseDto>> getEmployees(@RequestParam(defaultValue = "0") int page,

			@RequestParam(defaultValue = "5") int size,

			@RequestParam(defaultValue = "id") String sortBy) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

		return ResponseEntity.ok(service.getAllEmployees(pageable));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Long id,
			@Valid @RequestBody EmployeeRequestDto dto) {

		return ResponseEntity.ok(service.updateEmployee(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable Long id) {

		service.deleteEmployee(id);

		return ResponseEntity.ok(new ApiResponse("Employee deleted successfully"));
	}
}