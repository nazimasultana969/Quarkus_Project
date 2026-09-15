package com.example.employee.controller;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class EmployeeResourceTest {

	@Test
	void shouldGetEmployees() {

		given().when().get("/employees").then().statusCode(200);
	}

	@Test
	void shouldReturn404ForInvalidEmployee() {

		given().when().get("/employees/99999").then().statusCode(404).body("status", equalTo(404));
	}

	@Test
	void shouldCreateEmployee() {

		String request = """
				{
				    "name": "Alice",
				    "email": "alice@gmail.com",
				    "department": "HR"
				}
				""";

		given().contentType("application/json").body(request).when().post("/employees").then().statusCode(201)
				.body("name", equalTo("Alice"));
	}
}