package com.example.employee.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.employee.event.EmployeeCreatedEvent;
import com.example.employee.event.EmployeeDeletedEvent;

@Service
public class EmployeeEventPublisher {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public EmployeeEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void publishEmployeeCreated(EmployeeCreatedEvent event) {

		kafkaTemplate.send("employee-created-topic", String.valueOf(event.employeeId()), event);
	}

	public void publishEmployeeDeleted(EmployeeDeletedEvent event) {

		kafkaTemplate.send("employee-deleted-topic", String.valueOf(event.employeeId()), event);
	}
}