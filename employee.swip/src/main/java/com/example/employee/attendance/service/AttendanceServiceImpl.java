package com.example.employee.attendance.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.employee.attendance.dto.AttendanceReportResponse;
import com.example.employee.attendance.dto.AttendanceResponse;
import com.example.employee.attendance.entity.Attendance;
import com.example.employee.attendance.repository.AttendanceRepository;
import com.example.employee.attendance.repository.EmployeeReferenceRepository;
import com.example.employee.enums.AttendanceStatus;
import com.example.employee.exception.BusinessException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.swip.client.EmployeeClient;
import com.example.employee.swipe.dto.EmployeeResponseDto;

@Service
public class AttendanceServiceImpl implements AttendanceService {

	private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

	private final AttendanceRepository attendanceRepository;
	private final EmployeeClient employeeClient;
	private final EmployeeReferenceRepository employeeReferenceRepository;

	public AttendanceServiceImpl(AttendanceRepository attendanceRepository, EmployeeClient employeeClient,
			EmployeeReferenceRepository employeeReferenceRepository) {

		this.attendanceRepository = attendanceRepository;
		this.employeeClient = employeeClient;
		this.employeeReferenceRepository = employeeReferenceRepository;
	}

	@Override
	public AttendanceResponse checkIn(Long employeeId) {

		logger.info("Attendance check-in request received for employeeId={}", employeeId);

		EmployeeResponseDto employee = employeeClient.getEmployee(employeeId);

		if (employee == null) {

			throw new ResourceNotFoundException("Employee not found");
		}
		employeeReferenceRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		LocalDate today = LocalDate.now();

		Optional<Attendance> existingRecord = attendanceRepository.findByEmployeeIdAndAttendanceDate(employeeId, today);

		if (existingRecord.isPresent()) {

			logger.warn("Employee already checked in today employeeId={}", employeeId);

			throw new BusinessException("Already checked in today");
		}

		Attendance attendance = new Attendance();

		attendance.setEmployeeId(employeeId);
		attendance.setAttendanceDate(today);
		attendance.setCheckInTime(LocalDateTime.now());

		if (LocalTime.now().isAfter(LocalTime.of(9, 30))) {

			attendance.setAttendanceStatus(AttendanceStatus.LATE);

		} else {

			attendance.setAttendanceStatus(AttendanceStatus.PRESENT);
		}

		attendanceRepository.save(attendance);

		logger.info("Check-in successful employeeId={}", employeeId);

		return new AttendanceResponse("Check In Successful");
	}

	@Override
	public AttendanceResponse checkOut(Long employeeId) {

		logger.info("Checkout request received employeeId={}", employeeId);

		Attendance attendance = attendanceRepository.findByEmployeeIdAndAttendanceDate(employeeId, LocalDate.now())
				.orElseThrow(() -> new ResourceNotFoundException("Check In not found"));

		attendance.setCheckOutTime(LocalDateTime.now());

		Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());

		double totalHours = duration.toMinutes() / 60.0;

		attendance.setTotalHours(totalHours);

		if (totalHours < 4) {

			attendance.setAttendanceStatus(AttendanceStatus.HALF_DAY);
		}

		attendanceRepository.save(attendance);

		logger.info("Checkout successful employeeId={}, totalHours={}", employeeId, totalHours);

		return new AttendanceResponse("Check Out Successful");
	}

	@Override
	public AttendanceReportResponse generateReport(Long employeeId) {

		EmployeeResponseDto employee = employeeClient.getEmployee(employeeId);

		List<Attendance> records = attendanceRepository.findByEmployeeId(employeeId);

		long presentDays = records.stream().filter(a -> a.getAttendanceStatus() == AttendanceStatus.PRESENT).count();

		long lateDays = records.stream().filter(a -> a.getAttendanceStatus() == AttendanceStatus.LATE).count();

		long halfDays = records.stream().filter(a -> a.getAttendanceStatus() == AttendanceStatus.HALF_DAY).count();

		double totalHours = records.stream().mapToDouble(Attendance::getTotalHours).sum();

		return new AttendanceReportResponse(employeeId, employee.name(), presentDays, lateDays, halfDays, totalHours);
	}
}