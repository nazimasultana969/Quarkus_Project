package com.example.saga.payment.service;

import com.example.saga.payment.entity.Payment;
import com.example.saga.payment.repository.PaymentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

	private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

	private final PaymentRepository paymentRepository;

	public PaymentService(PaymentRepository paymentRepository) {

		this.paymentRepository = paymentRepository;
	}

	@Transactional
	public boolean processPayment(Long orderId) {

		try {

			Payment payment = new Payment();

			payment.setOrderId(orderId);

			/*
			 * Simple failure simulation.
			 */
			if (orderId == 2) {

				payment.setStatus("FAILED");

				paymentRepository.save(payment);

				log.warn("Payment failed. orderId={}", orderId);

				return false;
			}

			payment.setStatus("SUCCESS");

			paymentRepository.save(payment);

			log.info("Payment successful. orderId={}", orderId);

			return true;

		} catch (Exception e) {

			log.error("Payment processing failed", e);

			throw new RuntimeException("Payment processing failed", e);
		}
	}
}