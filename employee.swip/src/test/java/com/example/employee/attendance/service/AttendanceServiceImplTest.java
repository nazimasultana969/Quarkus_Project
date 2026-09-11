package com.example.employee.attendance.service;

import com.example.employee.attendance.dto.AttendanceResponse;
import com.example.employee.attendance.entity.Attendance;
import com.example.employee.attendance.repository.AttendanceRepository;
import com.example.employee.enums.AttendanceStatus;
import com.example.employee.exception.BusinessException;
import com.example.employee.swip.client.EmployeeClient;
import com.example.employee.swipe.dto.EmployeeResponseDto;

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
class AttendanceServiceImplTest {

	@Mock
	private AttendanceRepository attendanceRepository;

	@Mock
	private EmployeeClient employeeClient;

	@InjectMocks
	private AttendanceServiceImpl attendanceService;

	private EmployeeResponseDto employee;

	@BeforeEach
	void setUp() {

		employee = new EmployeeResponseDto(1L, "Nazima", "nazima@gmail.com", 50000.0);
	}

	@Test
	void checkInTest() {

		when(employeeClient.getEmployee(1L)).thenReturn(employee);

		when(attendanceRepository.findByEmployeeIdAndAttendanceDate(1L, LocalDate.now())).thenReturn(Optional.empty());

		AttendanceResponse response = attendanceService.checkIn(1L);

		assertNotNull(response);
		assertEquals("Check In Successful", response.message());

		verify(attendanceRepository, times(1)).save(any(Attendance.class));
	}

	@Test
	void checkInAlreadyExistsTest() {

		Attendance attendance = new Attendance();

		when(employeeClient.getEmployee(1L)).thenReturn(employee);

		when(attendanceRepository.findByEmployeeIdAndAttendanceDate(1L, LocalDate.now()))
				.thenReturn(Optional.of(attendance));

		assertThrows(BusinessException.class, () -> attendanceService.checkIn(1L));
	}

	@Test
	void checkOutTest() {

		Attendance attendance = new Attendance();

		attendance.setEmployeeId(1L);

		attendance.setCheckInTime(LocalDateTime.now().minusHours(8));

		when(attendanceRepository.findByEmployeeIdAndAttendanceDate(1L, LocalDate.now()))
				.thenReturn(Optional.of(attendance));

		AttendanceResponse response = attendanceService.checkOut(1L);

		assertEquals("Check Out Successful", response.message());

		verify(attendanceRepository).save(any(Attendance.class));
	}

	@Test
    void generateReportTest() {

        Attendance attendance =
                new Attendance();

        attendance.setAttendanceStatus(
                AttendanceStatus.PRESENT);

        attendance.setTotalHours(8.0);

        when(employeeClient.getEmployee(1L))
                .thenReturn(employee);

        when(attendanceRepository
                .findByEmployeeId(1L))
                .thenReturn(
                        java.util.List.of(
                                attendance));

        var report =
                attendanceService.generateReport(
                        1L);

        assertEquals(
                "Nazima",
                report.employeeName());

        assertEquals(
                1,
                report.presentDays());
    }
}