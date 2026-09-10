package com.example.jwt.service;

import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class AuthServiceTest {

	@Inject
	AuthService authService;

	@InjectMock
	UserRepository userRepository;

	@Test
	void login_shouldGenerateToken() {

		User user = new User("admin", "admin123", "ADMIN");

		when(userRepository.findByUsername("admin")).thenReturn(user);

		String token = authService.login("admin", "admin123");

		assertNotNull(token);
		assertFalse(token.isBlank());

		verify(userRepository).findByUsername("admin");
	}

	@Test
	void login_shouldFailWhenUserNotFound() {

		when(userRepository.findByUsername("admin")).thenReturn(null);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login("admin", "admin123"));

		assertEquals("Invalid username or password", exception.getMessage());
	}

	@Test
	void login_shouldFailWhenPasswordInvalid() {

		User user = new User("admin", "admin123", "ADMIN");

		when(userRepository.findByUsername("admin")).thenReturn(user);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login("admin", "wrong"));

		assertEquals("Invalid username or password", exception.getMessage());
	}
}