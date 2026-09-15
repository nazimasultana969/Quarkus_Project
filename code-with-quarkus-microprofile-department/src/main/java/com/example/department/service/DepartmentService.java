package com.example.department.service;

import com.example.department.dto.DepartmentResponse;
import com.example.department.exception.DepartmentNotFoundException;

import jakarta.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

@ApplicationScoped
public class DepartmentService {

    private static final Logger LOG =
            Logger.getLogger(DepartmentService.class);

    public DepartmentResponse getDepartment(Long id) {

        LOG.infof("Getting department: %d", id);

        if (id == 100) {

            return new DepartmentResponse(
                    100L,
                    "IT Department"
            );
        }

        LOG.errorf(
                "Department not found: %d",
                id
        );

        throw new DepartmentNotFoundException(
                "Department not found with id: " + id
        );
    }
}