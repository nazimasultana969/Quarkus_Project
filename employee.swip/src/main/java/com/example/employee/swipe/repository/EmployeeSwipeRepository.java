package com.example.employee.swipe.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee.swipe.entity.EmployeeSwipe;

public interface EmployeeSwipeRepository extends JpaRepository<EmployeeSwipe, Long> {

	Optional<EmployeeSwipe> findByEmployeeIdAndSwipeDate(Long employeeId, LocalDate swipeDate);
}
