package com.example.employee.service;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.entity.Employee;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

	@Mock
	private EmployeeRepository repository;

	@InjectMocks
	private EmployeeServiceImpl employeeService;

	private Employee employee;
	private EmployeeRequestDto requestDto;

	@BeforeEach
	void setUp() {

		employee = new Employee();
		employee.setId(1L);
		employee.setName("Nazima");
		employee.setEmail("nazima@gmail.com");
		employee.setSalary(50000.0);

		requestDto = new EmployeeRequestDto("Nazima", "nazima@gmail.com", 50000.0);
	}

	@Test
	void saveEmployeeTest() {

		when(repository.save(any(Employee.class))).thenReturn(employee);

		EmployeeResponseDto response = employeeService.saveEmployee(requestDto);

		assertNotNull(response);
		assertEquals("Nazima", response.name());

		verify(repository, times(1)).save(any(Employee.class));
	}

	@Test
	void getEmployeeTest() {

		when(repository.findById(1L)).thenReturn(Optional.of(employee));

		EmployeeResponseDto response = employeeService.getEmployee(1L);

		assertEquals(1L, response.id());
		assertEquals("Nazima", response.name());
	}

	@Test
	void getEmployeeNotFoundTest() {

		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployee(1L));
	}

	@Test
	void getAllEmployeesTest() {

		Page<Employee> employeePage = new PageImpl<>(List.of(employee));

		when(repository.findAll(any(PageRequest.class))).thenReturn(employeePage);

		Page<EmployeeResponseDto> result = employeeService.getAllEmployees(PageRequest.of(0, 5));

		assertEquals(1, result.getTotalElements());
	}

	@Test
	void updateEmployeeTest() {

		EmployeeRequestDto updateDto = new EmployeeRequestDto("Updated", "updated@gmail.com", 70000.0);

		when(repository.findById(1L)).thenReturn(Optional.of(employee));

		when(repository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

		EmployeeResponseDto response = employeeService.updateEmployee(1L, updateDto);

		assertEquals("Updated", response.name());

		assertEquals("updated@gmail.com", response.email());
	}

	@Test
	void deleteEmployeeTest() {

		when(repository.findById(1L)).thenReturn(Optional.of(employee));

		doNothing().when(repository).delete(employee);

		employeeService.deleteEmployee(1L);

		verify(repository, times(1)).delete(employee);
	}
}