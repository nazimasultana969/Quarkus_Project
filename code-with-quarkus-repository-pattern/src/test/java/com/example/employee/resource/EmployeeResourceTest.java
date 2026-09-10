package com.example.employee.resource;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class EmployeeResourceTest {

	@Test
	void shouldCreateEmployee() {

		String employee = """
				{
				    "name": "John",
				    "email": "john@test.com",
				    "department": "IT"
				}
				""";

		given().contentType("application/json").body(employee).when().post("/employees").then().statusCode(201)
				.body("name", equalTo("John")).body("email", equalTo("john@test.com"))
				.body("department", equalTo("IT"));
	}

	@Test
	void shouldGetAllEmployees() {

		given().when().get("/employees").then().statusCode(200);
	}
}