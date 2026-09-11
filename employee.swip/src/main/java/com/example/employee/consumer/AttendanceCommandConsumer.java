package com.example.employee.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.employee.attendance.entity.EmployeeReference;
import com.example.employee.event.AttendanceCreatedResult;
import com.example.employee.event.CreateAttendanceReferenceCommand;
import com.example.employee.attendance.repository.EmployeeReferenceRepository;

@Service
public class AttendanceCommandConsumer {

	private final EmployeeReferenceRepository repository;

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public AttendanceCommandConsumer(EmployeeReferenceRepository repository,
			KafkaTemplate<String, Object> kafkaTemplate) {

		this.repository = repository;
		this.kafkaTemplate = kafkaTemplate;
	}

	@KafkaListener(topics = "attendance-create-command", groupId = "attendance-service")
	public void createReference(CreateAttendanceReferenceCommand command) {

		try {

			EmployeeReference employee = new EmployeeReference();

			employee.setEmployeeId(command.employeeId());

			employee.setEmployeeName(command.name());

			employee.setEmail(command.email());

			repository.save(employee);

			AttendanceCreatedResult result = new AttendanceCreatedResult(command.sagaId(), command.employeeId(), true,
					"Attendance reference created");

			kafkaTemplate.send("attendance-create-result", String.valueOf(command.employeeId()), result);

		} catch (Exception ex) {

			AttendanceCreatedResult result = new AttendanceCreatedResult(command.sagaId(), command.employeeId(), false,
					ex.getMessage());

			kafkaTemplate.send("attendance-create-result", String.valueOf(command.employeeId()), result);
		}
	}
}