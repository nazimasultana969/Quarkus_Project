package com.example.employee.service;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.entity.Employee;
import com.example.employee.event.EmployeeCreatedEvent;
import com.example.employee.event.EmployeeDeletedEvent;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.publisher.EmployeeEventPublisher;
import com.example.employee.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository repository;
	
	private final EmployeeEventPublisher employeeEventPublisher;

	private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	public EmployeeServiceImpl(EmployeeRepository repository,EmployeeEventPublisher employeeEventPublisher) {

		this.repository = repository;
		this.employeeEventPublisher=employeeEventPublisher;
	}

	@Override
	public EmployeeResponseDto saveEmployee(EmployeeRequestDto dto) {

		log.info("Saving employee with email={}", dto.email());

		Employee employee = EmployeeMapper.toEntity(dto);

		Employee savedEmployee = repository.save(employee);
		employeeEventPublisher.publishEmployeeCreated(

				new EmployeeCreatedEvent(savedEmployee.getId(), savedEmployee.getName(), savedEmployee.getEmail()));

		log.info("Employee saved id={}", savedEmployee.getId());

		return EmployeeMapper.toDto(savedEmployee);
	}

	@Override
	public EmployeeResponseDto getEmployee(Long id) {

		log.info("Fetching employee {}", id);

		Employee employee = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found " + id));

		return EmployeeMapper.toDto(employee);
	}

	@Override
	public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {

		return repository.findAll(pageable).map(EmployeeMapper::toDto);
	}

	@Override
	public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto dto) {

		Employee employee = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		employee.setName(dto.name());
		employee.setEmail(dto.email());
		employee.setSalary(dto.salary());

		Employee updated = repository.save(employee);

		log.info("Updated employee id={}", id);

		return EmployeeMapper.toDto(updated);
	}

	@Override
	public void deleteEmployee(Long id) {

		Employee employee = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		repository.delete(employee);
		employeeEventPublisher.publishEmployeeDeleted(
				new EmployeeDeletedEvent(employee.getId()));
		log.info("Deleted employee id={}", id);
	}
}