package com.example.employee.attendance.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee.attendance.entity.EmployeeSaga;

public interface EmployeeSagaRepository extends JpaRepository<EmployeeSaga, UUID> {

}
