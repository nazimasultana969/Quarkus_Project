package com.example.employee.swip.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.employee.swipe.dto.EmployeeResponseDto;

@FeignClient(
        name = "employee-service",
        url = "http://localhost:8081")
public interface EmployeeClient {

    @GetMapping("/employees/{id}")
    EmployeeResponseDto getEmployee(
            @PathVariable Long id);
}