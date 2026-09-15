package com.example.employee.service;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class EmployeeResourceTest {

    @Test
    public void testGetEmployee() {

        given()
                .when()
                .get("/api/employees/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Nazima"));
    }

    @Test
    public void testEmployeeNotFound() {

        given()
                .when()
                .get("/api/employees/999")
                .then()
                .statusCode(404)
                .body("message",
                        is("Employee not found with id: 999"));
    }
}