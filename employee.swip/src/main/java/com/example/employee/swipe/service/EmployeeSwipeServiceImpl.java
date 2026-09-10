package com.example.employee.swipe.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.employee.exception.BusinessException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.swip.client.EmployeeClient;
import com.example.employee.swipe.dto.EmployeeResponseDto;
import com.example.employee.swipe.dto.SwipeResponse;
import com.example.employee.swipe.entity.EmployeeSwipe;
import com.example.employee.swipe.repository.EmployeeSwipeRepository;

@Service
public class EmployeeSwipeServiceImpl implements EmployeeSwipeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeSwipeServiceImpl.class);

	private final EmployeeSwipeRepository swipeRepository;
	private final EmployeeClient employeeClient;

	public EmployeeSwipeServiceImpl(EmployeeSwipeRepository swipeRepository, EmployeeClient employeeClient) {

		this.swipeRepository = swipeRepository;
		this.employeeClient = employeeClient;
	}

	@Override
	public SwipeResponse checkIn(Long employeeId) {

		logger.info("Swipe check-in request received employeeId={}", employeeId);

		EmployeeResponseDto employee = employeeClient.getEmployee(employeeId);

		if (employee == null) {

			logger.error("Employee not found employeeId={}", employeeId);

			throw new ResourceNotFoundException("Employee not found");
		}

		LocalDate today = LocalDate.now();

		swipeRepository.findByEmployeeIdAndSwipeDate(employeeId, today).ifPresent(record -> {

			logger.warn("Duplicate check-in employeeId={}", employeeId);

			throw new BusinessException("Employee already checked in today");
		});

		EmployeeSwipe swipe = new EmployeeSwipe();

		swipe.setEmployeeId(employeeId);
		swipe.setSwipeDate(today);
		swipe.setInTime(LocalDateTime.now());

		if (LocalDateTime.now().getHour() >= 10) {

			swipe.setStatus("LATE");

		} else {

			swipe.setStatus("PRESENT");
		}

		swipeRepository.save(swipe);

		logger.info("Employee checked in employeeId={}", employeeId);

		return new SwipeResponse("Check-in successful");
	}

	@Override
	public SwipeResponse checkOut(Long employeeId) {

		logger.info("Swipe checkout request received employeeId={}", employeeId);

		EmployeeSwipe swipe = swipeRepository.findByEmployeeIdAndSwipeDate(employeeId, LocalDate.now())
				.orElseThrow(() -> {

					logger.error("No check-in found employeeId={}", employeeId);

					return new ResourceNotFoundException("No check-in found for today");
				});

		if (swipe.getOutTime() != null) {

			logger.warn("Already checked out employeeId={}", employeeId);

			throw new BusinessException("Already checked out");
		}

		swipe.setOutTime(LocalDateTime.now());

		Duration duration = Duration.between(swipe.getInTime(), swipe.getOutTime());

		double totalHours = duration.toMinutes() / 60.0;

		swipe.setTotalHours(totalHours);

		swipeRepository.save(swipe);

		logger.info("Swipe checkout completed employeeId={}, totalHours={}", employeeId, totalHours);

		return new SwipeResponse("Check-out successful");
	}
}