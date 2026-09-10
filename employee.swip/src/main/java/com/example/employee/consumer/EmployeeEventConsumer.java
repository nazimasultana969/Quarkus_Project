package com.example.employee.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.employee.attendance.entity.EmployeeReference;
import com.example.employee.attendance.repository.EmployeeReferenceRepository;
import com.example.employee.event.EmployeeCreatedEvent;
import com.example.employee.event.EmployeeDeletedEvent;

@Service
public class EmployeeEventConsumer {

	private final EmployeeReferenceRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(EmployeeEventConsumer.class);

	public EmployeeEventConsumer(EmployeeReferenceRepository repository) {

		this.repository = repository;
	}

	@KafkaListener(topics = "employee-created-topic", containerFactory = "employeeCreatedKafkaListenerContainerFactory")
	public void consumeEmployeeCreated(EmployeeCreatedEvent event) {

		logger.info("Employee Created Event Received : {}", event);

		EmployeeReference employee = new EmployeeReference();

		employee.setEmployeeId(event.employeeId());
		employee.setEmployeeName(event.name());
		employee.setEmail(event.email());

		repository.save(employee);

		logger.debug("Saved in employee reference table");
	}

	@KafkaListener(topics = "employee-deleted-topic", containerFactory = "employeeDeletedKafkaListenerContainerFactory")
	public void consumeEmployeeDeleted(EmployeeDeletedEvent event) {

		logger.info("Employee Deleted Event Received : {}", event);

		repository.deleteById(event.employeeId());

		logger.debug("Deleted successfully");
	}
}