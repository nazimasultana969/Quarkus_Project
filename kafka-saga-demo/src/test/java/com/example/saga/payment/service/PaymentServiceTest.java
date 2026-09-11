package com.example.saga.payment.service;

import com.example.saga.payment.entity.Payment;
import com.example.saga.payment.repository.PaymentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@InjectMocks
	private PaymentService paymentService;

	@Test
	void shouldProcessPaymentSuccessfully() {

		when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

		boolean result = paymentService.processPayment(1L);

		assertTrue(result);

		ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);

		verify(paymentRepository).save(captor.capture());

		Payment payment = captor.getValue();

		assertEquals(1L, payment.getOrderId());

		assertEquals("SUCCESS", payment.getStatus());
	}

	@Test
	void shouldFailPaymentForOrderTwo() {

		when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

		boolean result = paymentService.processPayment(2L);

		assertFalse(result);

		ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);

		verify(paymentRepository).save(captor.capture());

		Payment payment = captor.getValue();

		assertEquals(2L, payment.getOrderId());

		assertEquals("FAILED", payment.getStatus());
	}

	@Test
	void shouldThrowExceptionWhenPaymentSaveFails() {

		when(paymentRepository.save(any(Payment.class))).thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> paymentService.processPayment(1L));

		verify(paymentRepository, times(1)).save(any(Payment.class));
	}
}