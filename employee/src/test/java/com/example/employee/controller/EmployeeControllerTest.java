package com.example.employee.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.service.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeServiceImpl employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEmployeeTest() throws Exception {

        EmployeeRequestDto request =
                new EmployeeRequestDto(
                        "Nazima",
                        "nazima@gmail.com",
                        50000.0);

        EmployeeResponseDto response =
                new EmployeeResponseDto(
                        1L,
                        "Nazima",
                        "nazima@gmail.com",
                        50000.0);

        when(employeeService.saveEmployee(request))
                .thenReturn(response);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                .value("Nazima"));
    }

    @Test
    void getEmployeeByIdTest() throws Exception {

        EmployeeResponseDto response =
                new EmployeeResponseDto(
                        1L,
                        "Nazima",
                        "nazima@gmail.com",
                        50000.0);

        when(employeeService.getEmployee(1L))
                .thenReturn(response);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email")
                .value("nazima@gmail.com"));
    }

    @Test
    void getAllEmployeesTest() throws Exception {

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk());
    }

    @Test
	void deleteEmployeeTest() throws Exception {

		doNothing().when(employeeService).deleteEmployee(1L);
		mockMvc.perform(delete("/employees/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Employee deleted successfully"));
	}
}