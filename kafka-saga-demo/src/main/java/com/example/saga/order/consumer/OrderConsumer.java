package com.example.saga.order.consumer;

import com.example.saga.common.SagaEvent;
import com.example.saga.order.entity.Order;
import com.example.saga.order.repository.OrderRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderConsumer {

	private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

	private final OrderRepository orderRepository;

	private final ObjectMapper objectMapper;

	public OrderConsumer(OrderRepository orderRepository, ObjectMapper objectMapper) {

		this.orderRepository = orderRepository;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = "payment-topic", groupId = "order-group")
	@Transactional
	public void consumePayment(String message) {

		try {

			SagaEvent event = objectMapper.readValue(message, SagaEvent.class);

			Order order = orderRepository.findById(event.orderId())
					.orElseThrow(() -> new RuntimeException("Order not found"));

			if ("PAYMENT_SUCCESS".equals(event.eventType())) {

				order.setStatus("COMPLETED");

				log.info("Order completed. orderId={}", order.getId());

			} else if ("PAYMENT_FAILED".equals(event.eventType())) {

				order.setStatus("FAILED");

				log.warn("Order failed. orderId={}", order.getId());
			}

			orderRepository.save(order);

		} catch (Exception e) {

			log.error("Order consumer failed", e);

			throw new RuntimeException(e);
		}
	}
}