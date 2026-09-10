package com.example.employee.repository;

import com.example.employee.entity.Employee;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepository<Employee> {

	public List<Employee> findEmployees(String name, String email, String department, int page, int size) {

		StringBuilder query = new StringBuilder("1 = 1");

		java.util.Map<String, Object> params = new java.util.HashMap<>();

		if (name != null && !name.isBlank()) {

			query.append(" and lower(name) like :name");

			params.put("name", "%" + name.toLowerCase() + "%");
		}

		if (email != null && !email.isBlank()) {

			query.append(" and lower(email) like :email");

			params.put("email", "%" + email.toLowerCase() + "%");
		}

		if (department != null && !department.isBlank()) {

			query.append(" and lower(department) = :department");

			params.put("department", department.toLowerCase());
		}

		return find(query.toString(), params).page(Page.of(page, size)).list();
	}

	public long countEmployees(String name, String email, String department) {

		StringBuilder query = new StringBuilder("1 = 1");

		java.util.Map<String, Object> params = new java.util.HashMap<>();

		if (name != null && !name.isBlank()) {

			query.append(" and lower(name) like :name");

			params.put("name", "%" + name.toLowerCase() + "%");
		}

		if (email != null && !email.isBlank()) {

			query.append(" and lower(email) like :email");

			params.put("email", "%" + email.toLowerCase() + "%");
		}

		if (department != null && !department.isBlank()) {

			query.append(" and lower(department) = :department");

			params.put("department", department.toLowerCase());
		}

		return count(query.toString(), params);
	}
}