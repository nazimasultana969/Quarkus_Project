package com.example.employee.swipe.service;

import com.example.employee.swipe.dto.SwipeResponse;

public interface EmployeeSwipeService {

    SwipeResponse checkIn(Long employeeId);

    SwipeResponse checkOut(Long employeeId);
}