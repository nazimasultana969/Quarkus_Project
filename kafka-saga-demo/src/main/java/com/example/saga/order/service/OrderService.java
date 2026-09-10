package com.example.saga.order.service;

import com.example.saga.common.SagaEvent;
import com.example.saga.order.entity.Order;
import com.example.saga.order.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

	private final OrderRepository orderRepository;

	private final KafkaTemplate<String, String> kafkaTemplate;

	public OrderService(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate) {

		this.orderRepository = orderRepository;
		this.kafkaTemplate = kafkaTemplate;
	}

	@Transactional
	public Order createOrder(Long productId, Integer quantity) {

		try {

			Order order = new Order();

			order.setProductId(productId);
			order.setQuantity(quantity);
			order.setStatus("CREATED");

			Order savedOrder = orderRepository.save(order);

			log.info("Order created. orderId={}", savedOrder.getId());

			SagaEvent event = new SagaEvent(savedOrder.getId(), productId, quantity, "ORDER_CREATED");

			kafkaTemplate.send("order-topic", savedOrder.getId().toString(), convertToJson(event));

			log.info("ORDER_CREATED event published. orderId={}", savedOrder.getId());

			return savedOrder;

		} catch (Exception e) {

			log.error("Order creation failed", e);

			throw new RuntimeException("Order creation failed", e);
		}
	}

	private String convertToJson(SagaEvent event) {

		return String.format("{\"orderId\":%d,\"productId\":%d,\"quantity\":%d,\"eventType\":\"%s\"}",
				event.orderId(), event.productId(), event.quantity(), event.eventType());
	}
}