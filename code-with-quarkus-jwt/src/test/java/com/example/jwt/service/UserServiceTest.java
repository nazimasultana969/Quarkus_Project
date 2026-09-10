package com.example.jwt.service;

import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class UserServiceTest {

	@Inject
	UserService userService;

	@InjectMock
	UserRepository userRepository;

	@Test
	void getAllUsers_shouldReturnUsers() {

		List<User> users = List.of(new User("admin", "admin123", "ADMIN"), new User("user", "user123", "USER"));

		when(userRepository.listAll()).thenReturn(users);

		List<User> result = userService.getAllUsers();

		assertEquals(2, result.size());

		verify(userRepository).listAll();
	}

	@Test
	void getUser_shouldReturnUser() {

		User user = new User("admin", "admin123", "ADMIN");

		user.id = 1L;

		when(userRepository.findById(1L)).thenReturn(user);

		User result = userService.getUser(1L);

		assertNotNull(result);
		assertEquals("admin", result.username);

		verify(userRepository).findById(1L);
	}

	@Test
	void getUser_shouldThrowExceptionWhenNotFound() {

		when(userRepository.findById(100L)).thenReturn(null);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUser(100L));

		assertEquals("User not found", exception.getMessage());
	}

	@Test
	void createUser_shouldPersistUser() {

		User user = new User("john", "john123", "USER");

		User result = userService.createUser(user);

		assertNotNull(result);
		assertEquals("john", result.username);

		verify(userRepository).persist(user);
	}
}