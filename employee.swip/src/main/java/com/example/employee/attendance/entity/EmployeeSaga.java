package com.example.employee.attendance.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.employee.enums.SagaStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_saga")
public class EmployeeSaga {

	@Id
	private UUID sagaId;

	private Long employeeId;

	@Enumerated(EnumType.STRING)
	private SagaStatus status;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public UUID getSagaId() {
		return sagaId;
	}

	public void setSagaId(UUID sagaId) {
		this.sagaId = sagaId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public SagaStatus getStatus() {
		return status;
	}

	public void setStatus(SagaStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
