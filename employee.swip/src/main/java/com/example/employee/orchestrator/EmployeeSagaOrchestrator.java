package com.example.employee.orchestrator;


import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.employee.attendance.entity.EmployeeSaga;
import com.example.employee.attendance.repository.EmployeeSagaRepository;
import com.example.employee.enums.SagaStatus;
import com.example.employee.event.AttendanceCreatedResult;
import com.example.employee.event.CreateAttendanceReferenceCommand;
import com.example.employee.event.CreateEmployeeCommand;
import com.example.employee.event.DeleteEmployeeCommand;
import com.example.employee.event.EmployeeCreatedResult;
import com.example.employee.event.EmployeeDeletedResult;


@Service
public class EmployeeSagaOrchestrator {

	private final EmployeeSagaRepository sagaRepository;

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public EmployeeSagaOrchestrator(EmployeeSagaRepository sagaRepository,
			KafkaTemplate<String, Object> kafkaTemplate) {

		this.sagaRepository = sagaRepository;
		this.kafkaTemplate = kafkaTemplate;
	}

	public UUID startSaga(Long employeeId, String name, String email) {

		UUID sagaId = UUID.randomUUID();

		EmployeeSaga saga = new EmployeeSaga();

		saga.setSagaId(sagaId);
		saga.setEmployeeId(employeeId);
		saga.setStatus(SagaStatus.EMPLOYEE_CREATE_SENT);

		saga.setCreatedAt(LocalDateTime.now());

		saga.setUpdatedAt(LocalDateTime.now());

		sagaRepository.save(saga);

		CreateEmployeeCommand command = new CreateEmployeeCommand(
				sagaId, employeeId, name, email);

		kafkaTemplate.send("employee-create-command", String.valueOf(employeeId), command);

		return sagaId;
	}

	public void handleEmployeeCreated(EmployeeCreatedResult result) {

		EmployeeSaga saga = sagaRepository.findById(result.sagaId()).orElseThrow();

		if (!result.success()) {

			saga.setStatus(SagaStatus.FAILED);

			saga.setUpdatedAt(LocalDateTime.now());

			sagaRepository.save(saga);

			return;
		}

		saga.setStatus(SagaStatus.EMPLOYEE_CREATED);

		saga.setUpdatedAt(LocalDateTime.now());

		sagaRepository.save(saga);

		CreateAttendanceReferenceCommand command = new CreateAttendanceReferenceCommand(result.sagaId(),
				result.employeeId(), result.name(), result.email());

		saga.setStatus(SagaStatus.ATTENDANCE_CREATE_SENT);

		saga.setUpdatedAt(LocalDateTime.now());

		sagaRepository.save(saga);

		kafkaTemplate.send("attendance-create-command", String.valueOf(result.employeeId()), command);
	}

	public void handleAttendanceCreated(AttendanceCreatedResult result) {

		EmployeeSaga saga = sagaRepository.findById(result.sagaId()).orElseThrow();

		if (result.success()) {

			saga.setStatus(SagaStatus.COMPLETED);

			saga.setUpdatedAt(LocalDateTime.now());

			sagaRepository.save(saga);

			return;
		}

		// Attendance failed
		saga.setStatus(SagaStatus.COMPENSATING);

		saga.setUpdatedAt(LocalDateTime.now());

		sagaRepository.save(saga);

		DeleteEmployeeCommand command = new DeleteEmployeeCommand(result.sagaId(), result.employeeId());

		kafkaTemplate.send("employee-delete-command", String.valueOf(result.employeeId()), command);
	}

	public void handleEmployeeDeleted(EmployeeDeletedResult result) {

		EmployeeSaga saga = sagaRepository.findById(result.sagaId()).orElseThrow();

		if (result.success()) {

			saga.setStatus(SagaStatus.COMPENSATED);

		} else {

			saga.setStatus(SagaStatus.FAILED);
		}

		saga.setUpdatedAt(LocalDateTime.now());

		sagaRepository.save(saga);
	}
}
