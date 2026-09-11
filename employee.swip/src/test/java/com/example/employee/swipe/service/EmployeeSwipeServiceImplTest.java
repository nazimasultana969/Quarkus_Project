package com.example.employee.swipe.service;

import com.example.employee.exception.BusinessException;
import com.example.employee.swip.client.EmployeeClient;
import com.example.employee.swipe.dto.EmployeeResponseDto;
import com.example.employee.swipe.dto.SwipeResponse;
import com.example.employee.swipe.entity.EmployeeSwipe;
import com.example.employee.swipe.repository.EmployeeSwipeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeSwipeServiceImplTest {

	@Mock
	private EmployeeSwipeRepository swipeRepository;

	@Mock
	private EmployeeClient employeeClient;

	@InjectMocks
	private EmployeeSwipeServiceImpl swipeService;

	private EmployeeResponseDto employee;

	@BeforeEach
	void setUp() {

		employee = new EmployeeResponseDto(1L, "Nazima", "nazima@gmail.com", 50000.0);
	}

	@Test
	void checkInTest() {

		when(employeeClient.getEmployee(1L)).thenReturn(employee);

		when(swipeRepository.findByEmployeeIdAndSwipeDate(1L, LocalDate.now())).thenReturn(Optional.empty());

		SwipeResponse response = swipeService.checkIn(1L);

		assertEquals("Check-in successful", response.message());

		verify(swipeRepository).save(any(EmployeeSwipe.class));
	}

	@Test
	void checkInDuplicateTest() {

		EmployeeSwipe swipe = new EmployeeSwipe();

		when(employeeClient.getEmployee(1L)).thenReturn(employee);

		when(swipeRepository.findByEmployeeIdAndSwipeDate(1L, LocalDate.now())).thenReturn(Optional.of(swipe));

		assertThrows(BusinessException.class, () -> swipeService.checkIn(1L));
	}

	@Test
	void checkOutTest() {

		EmployeeSwipe swipe = new EmployeeSwipe();

		swipe.setEmployeeId(1L);

		swipe.setInTime(LocalDateTime.now().minusHours(8));

		when(swipeRepository.findByEmployeeIdAndSwipeDate(1L, LocalDate.now())).thenReturn(Optional.of(swipe));

		SwipeResponse response = swipeService.checkOut(1L);

		assertEquals("Check-out successful", response.message());

		verify(swipeRepository).save(any(EmployeeSwipe.class));
	}

	@Test
	void checkOutAlreadyDoneTest() {

		EmployeeSwipe swipe = new EmployeeSwipe();

		swipe.setOutTime(LocalDateTime.now());

		when(swipeRepository.findByEmployeeIdAndSwipeDate(1L, LocalDate.now())).thenReturn(Optional.of(swipe));

		assertThrows(BusinessException.class, () -> swipeService.checkOut(1L));
	}
}