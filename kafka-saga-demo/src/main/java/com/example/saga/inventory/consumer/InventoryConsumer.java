package com.example.saga.inventory.consumer;

import com.example.saga.common.SagaEvent;
import com.example.saga.inventory.service.InventoryService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryConsumer {

	private static final Logger log = LoggerFactory.getLogger(InventoryConsumer.class);

	private final InventoryService inventoryService;

	private final KafkaTemplate<String, String> kafkaTemplate;

	private final ObjectMapper objectMapper;

	public InventoryConsumer(InventoryService inventoryService, KafkaTemplate<String, String> kafkaTemplate,
			ObjectMapper objectMapper) {

		this.inventoryService = inventoryService;
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = "order-topic", groupId = "inventory-group")
	public void consumeOrder(String message) {

		try {

			SagaEvent event = objectMapper.readValue(message, SagaEvent.class);

			log.info("Inventory received ORDER_CREATED. orderId={}", event.orderId());

			boolean reserved = inventoryService.reserveInventory(event.productId(), event.quantity());

			if (reserved) {

				SagaEvent inventoryEvent = new SagaEvent(event.orderId(), event.productId(), event.quantity(),
						"INVENTORY_RESERVED");

				kafkaTemplate.send("inventory-topic", event.orderId().toString(),
						objectMapper.writeValueAsString(inventoryEvent));

				log.info("INVENTORY_RESERVED published. orderId={}", event.orderId());

			} else {

				SagaEvent failedEvent = new SagaEvent(event.orderId(), event.productId(), event.quantity(),
						"INVENTORY_FAILED");

				kafkaTemplate.send("inventory-topic", event.orderId().toString(),
						objectMapper.writeValueAsString(failedEvent));

				log.warn("Inventory reservation failed. orderId={}", event.orderId());
			}

		} catch (Exception e) {

			log.error("Error consuming ORDER_CREATED", e);

			throw new RuntimeException(e);
		}
	}

	@KafkaListener(topics = "payment-topic", groupId = "inventory-group")
	public void consumePayment(String message) {

		try {

			SagaEvent event = objectMapper.readValue(message, SagaEvent.class);

			if (!"PAYMENT_FAILED".equals(event.eventType())) {

				return;
			}

			log.info("Payment failed. Starting inventory compensation. orderId={}", event.orderId());

			inventoryService.releaseInventory(event.productId(), event.quantity());

			SagaEvent rollbackEvent = new SagaEvent(event.orderId(), event.productId(), event.quantity(),
					"INVENTORY_RELEASED");

			kafkaTemplate.send("inventory-topic", event.orderId().toString(),
					objectMapper.writeValueAsString(rollbackEvent));

			log.info("Inventory compensation completed. orderId={}", event.orderId());

		} catch (Exception e) {

			log.error("Inventory compensation failed", e);

			throw new RuntimeException(e);
		}
	}
}