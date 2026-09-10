package com.example.employee.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee.attendance.entity.EmployeeReference;

public interface EmployeeReferenceRepository extends JpaRepository<EmployeeReference, Long> {
}