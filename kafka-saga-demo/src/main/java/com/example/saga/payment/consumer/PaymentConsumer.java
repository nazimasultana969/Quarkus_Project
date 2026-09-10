package com.example.saga.payment.consumer;

import com.example.saga.common.SagaEvent;
import com.example.saga.payment.service.PaymentService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {

	private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);

	private final PaymentService paymentService;

	private final KafkaTemplate<String, String> kafkaTemplate;

	private final ObjectMapper objectMapper;

	public PaymentConsumer(PaymentService paymentService, KafkaTemplate<String, String> kafkaTemplate,
			ObjectMapper objectMapper) {

		this.paymentService = paymentService;
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = "inventory-topic", groupId = "payment-group")
	public void consumeInventory(String message) {

		try {

			SagaEvent event = objectMapper.readValue(message, SagaEvent.class);

			if (!"INVENTORY_RESERVED".equals(event.eventType())) {

				return;
			}

			log.info("Payment received INVENTORY_RESERVED. orderId={}", event.orderId());

			boolean paymentSuccess = paymentService.processPayment(event.orderId());

			String eventType = paymentSuccess ? "PAYMENT_SUCCESS" : "PAYMENT_FAILED";

			SagaEvent paymentEvent = new SagaEvent(event.orderId(), event.productId(), event.quantity(), eventType);

			kafkaTemplate.send("payment-topic", event.orderId().toString(),
					objectMapper.writeValueAsString(paymentEvent));

			log.info("{} published. orderId={}", eventType, event.orderId());

		} catch (Exception e) {

			log.error("Payment consumer failed", e);

			throw new RuntimeException(e);
		}
	}
}