package com.example.employee.swipe.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.employee.swipe.dto.SwipeResponse;
import com.example.employee.swipe.service.EmployeeSwipeService;

@WebMvcTest(EmployeeSwipeController.class)
class SwipeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private EmployeeSwipeService swipeService;

	@Test
	void checkInTest() throws Exception {

		SwipeResponse response = new SwipeResponse("Check-in successful");

		when(swipeService.checkIn(1L)).thenReturn(response);

		mockMvc.perform(post("/swipes/1/checkin")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Check-in successful"));
	}

	@Test
	void checkOutTest() throws Exception {

		SwipeResponse response = new SwipeResponse("Check-out successful");

		when(swipeService.checkOut(1L)).thenReturn(response);

		mockMvc.perform(post("/swipes/1/checkout")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Check-out successful"));
	}
}