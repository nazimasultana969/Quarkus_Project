package com.example.employee.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.employee.entity.Employee;
import com.example.employee.event.CreateEmployeeCommand;
import com.example.employee.event.DeleteEmployeeCommand;
import com.example.employee.event.EmployeeCreatedResult;
import com.example.employee.event.EmployeeDeletedResult;
import com.example.employee.repository.EmployeeRepository;

@Service
public class EmployeeCommandConsumer {

	private final EmployeeRepository employeeRepository;

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public EmployeeCommandConsumer(EmployeeRepository employeeRepository, KafkaTemplate<String, Object> kafkaTemplate) {

		this.employeeRepository = employeeRepository;
		this.kafkaTemplate = kafkaTemplate;
	}

	@KafkaListener(topics = "employee-create-command", groupId = "employee-service")
	public void createEmployee(CreateEmployeeCommand command) {

		try {

			Employee employee = new Employee();

			employee.setId(command.employeeId());
			employee.setName(command.name());
			employee.setEmail(command.email());

			employeeRepository.save(employee);

			EmployeeCreatedResult result = new EmployeeCreatedResult(command.sagaId(), command.employeeId(),
					command.name(), command.email(), true, "Employee created successfully");

			kafkaTemplate.send("employee-create-result", String.valueOf(command.employeeId()), result);

		} catch (Exception ex) {

			EmployeeCreatedResult result = new EmployeeCreatedResult(command.sagaId(), command.employeeId(),
					command.name(), command.email(), false, ex.getMessage());

			kafkaTemplate.send("employee-create-result", String.valueOf(command.employeeId()), result);
		}
	}

	@KafkaListener(topics = "employee-delete-command", groupId = "employee-service")
	public void deleteEmployee(DeleteEmployeeCommand command) {

		try {

			employeeRepository.deleteById(command.employeeId());

			EmployeeDeletedResult result = new EmployeeDeletedResult(command.sagaId(), command.employeeId(), true,
					"Employee deleted successfully");

			kafkaTemplate.send("employee-delete-result", String.valueOf(command.employeeId()), result);

		} catch (Exception ex) {

			EmployeeDeletedResult result = new EmployeeDeletedResult(command.sagaId(), command.employeeId(), false,
					ex.getMessage());

			kafkaTemplate.send("employee-delete-result", String.valueOf(command.employeeId()), result);
		}
	}
}