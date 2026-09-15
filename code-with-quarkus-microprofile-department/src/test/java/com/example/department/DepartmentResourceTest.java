package com.example.department;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class DepartmentResourceTest {

    @Test
    public void testGetDepartment() {

        given()
                .when()
                .get("/api/departments/100")
                .then()
                .statusCode(200)
                .body("id", is(100))
                .body("name", is("IT Department"));
    }

    @Test
    public void testDepartmentNotFound() {

        given()
                .when()
                .get("/api/departments/999")
                .then()
                .statusCode(404)
                .body(
                        "message",
                        is("Department not found with id: 999")
                );
    }
}