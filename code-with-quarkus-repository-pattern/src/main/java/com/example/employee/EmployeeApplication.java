package com.example.employee;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class EmployeeApplication {

	public static void main(String[] args) {

		Quarkus.run(args);
	}
}