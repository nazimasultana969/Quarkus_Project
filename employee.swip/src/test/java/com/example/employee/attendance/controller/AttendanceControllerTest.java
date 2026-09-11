package com.example.employee.attendance.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.employee.attendance.dto.AttendanceReportResponse;
import com.example.employee.attendance.dto.AttendanceResponse;
import com.example.employee.attendance.service.AttendanceService;

@WebMvcTest(AttendanceController.class)
class AttendanceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AttendanceService attendanceService;

	@Test
	void checkInTest() throws Exception {

		AttendanceResponse response = new AttendanceResponse("Check In Successful");

		when(attendanceService.checkIn(1L)).thenReturn(response);

		mockMvc.perform(post("/attendance/1/checkin")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Check In Successful"));
	}

	@Test
	void checkOutTest() throws Exception {

		AttendanceResponse response = new AttendanceResponse("Check Out Successful");

		when(attendanceService.checkOut(1L)).thenReturn(response);

		mockMvc.perform(post("/attendance/1/checkout")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Check Out Successful"));
	}

	@Test
	void attendanceReportTest() throws Exception {

		AttendanceReportResponse response = new AttendanceReportResponse(1L, "Nazima", 20, 2, 1, 160.5);

		when(attendanceService.generateReport(1L)).thenReturn(response);

		mockMvc.perform(get("/attendance/1/report")).andExpect(status().isOk())
				.andExpect(jsonPath("$.employeeId").value(1)).andExpect(jsonPath("$.employeeName").value("Nazima"));
	}
}