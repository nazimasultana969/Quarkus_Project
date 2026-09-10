package com.example.employee.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "emp_tab")
public class EmployeeActiveRecord extends PanacheEntity {

	@Column(nullable = false)
	public String name;

	@Column(nullable = false, unique = true)
	public String email;

	public String department;

	public void updateEmployee(String name, String email, String department) {

		this.name = name;
		this.email = email;
		this.department = department;
	}


	public static List<EmployeeActiveRecord> findAllEmployees() {

		return listAll();
	}

	public static EmployeeActiveRecord findEmployeeById(Long id) {

		return findById(id);
	}

	public static void createEmployee(EmployeeActiveRecord employee) {

		employee.persist();
	}

	public void deleteEmployee() {

		 delete();
	}
}