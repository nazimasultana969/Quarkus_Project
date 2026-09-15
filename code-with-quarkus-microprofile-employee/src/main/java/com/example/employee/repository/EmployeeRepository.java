package com.example.employee.repository;

import com.example.employee.entity.Employee;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepository<Employee> {

    public Employee findByEmail(String email) {
        return find("email", email).firstResult();
    }
}