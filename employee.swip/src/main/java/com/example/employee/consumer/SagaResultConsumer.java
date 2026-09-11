package com.example.employee.consumer;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.employee.event.AttendanceCreatedResult;
import com.example.employee.event.EmployeeCreatedResult;
import com.example.employee.event.EmployeeDeletedResult;
import com.example.employee.orchestrator.EmployeeSagaOrchestrator;

@Service
public class SagaResultConsumer {

    private final EmployeeSagaOrchestrator orchestrator;

    public SagaResultConsumer(
            EmployeeSagaOrchestrator orchestrator) {

        this.orchestrator = orchestrator;
    }

    @KafkaListener(
            topics = "employee-create-result",
            groupId = "employee-saga-orchestrator"
    )
    public void employeeCreated(
            EmployeeCreatedResult result) {

        orchestrator.handleEmployeeCreated(result);
    }

    @KafkaListener(
            topics = "attendance-create-result",
            groupId = "employee-saga-orchestrator"
    )
    public void attendanceCreated(
            AttendanceCreatedResult result) {

        orchestrator.handleAttendanceCreated(result);
    }

    @KafkaListener(
            topics = "employee-delete-result",
            groupId = "employee-saga-orchestrator"
    )
    public void employeeDeleted(
            EmployeeDeletedResult result) {

        orchestrator.handleEmployeeDeleted(result);
    }
}
