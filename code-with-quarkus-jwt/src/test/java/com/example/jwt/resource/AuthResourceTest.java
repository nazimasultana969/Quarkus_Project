package com.example.jwt.resource;

import com.example.jwt.service.AuthService;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.*;

@QuarkusTest
class AuthResourceTest {

	@InjectMock
	AuthService authService;

	@Test
	void login_shouldReturnJwt() {

		when(authService.login("admin", "admin123")).thenReturn("mock-jwt-token");

		given().contentType(MediaType.APPLICATION_JSON).body("""
				    {
				        "username": "admin",
				        "password": "admin123"
				    }
				""").when().post("/auth/login").then().statusCode(200).body("token", equalTo("mock-jwt-token"));

		verify(authService).login("admin", "admin123");
	}

	@Test
	void login_shouldReturnBadRequestForInvalidUser() {

		when(authService.login("admin", "wrong")).thenThrow(new RuntimeException("Invalid username or password"));

		given().contentType(MediaType.APPLICATION_JSON).body("""
				    {
				        "username": "admin",
				        "password": "wrong"
				    }
				""").when().post("/auth/login").then().statusCode(400).body("message",
				equalTo("Invalid username or password"));
	}
}