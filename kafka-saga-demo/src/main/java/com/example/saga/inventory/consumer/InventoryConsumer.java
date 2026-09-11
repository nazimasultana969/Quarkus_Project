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

	/*
	 * STEP 1
	 *
	 * Receive ORDER_CREATED Reserve inventory Publish INVENTORY_RESERVED
	 */
	@KafkaListener(topics = "order-topic", groupId = "inventory-group")
	public void consumeOrder(String message) {

		try {

			log.info("Inventory received message: {}", message);

			SagaEvent event = objectMapper.readValue(message, SagaEvent.class);

			if (!"ORDER_CREATED".equals(event.eventType())) {
				return;
			}

			boolean reserved = inventoryService.reserveInventory(event.productId(), event.quantity());

			if (reserved) {

				SagaEvent inventoryEvent = new SagaEvent(event.orderId(), event.productId(), event.quantity(),
						"INVENTORY_RESERVED");

				kafkaTemplate.send("inventory-topic", event.orderId().toString(),
						objectMapper.writeValueAsString(inventoryEvent));

				log.info("Inventory reserved. orderId={}", event.orderId());

			} else {

				/*
				 * Inventory failed.
				 *
				 * No payment should happen. Inform Order that order failed.
				 */
				SagaEvent failedEvent = new SagaEvent(event.orderId(), event.productId(), event.quantity(),
						"INVENTORY_FAILED");

				kafkaTemplate.send("payment-topic", event.orderId().toString(),
						objectMapper.writeValueAsString(failedEvent));

				log.warn("Inventory reservation failed. orderId={}", event.orderId());
			}

		} catch (Exception e) {

			log.error("Error while consuming order event", e);

			throw new RuntimeException("Inventory consumer failed", e);
		}
	}

	/*
	 * STEP 4
	 *
	 * Payment failed. Release previously reserved inventory.
	 */
	@KafkaListener(topics = "payment-topic", groupId = "inventory-group")
	public void consumePaymentFailure(String message) {

		try {

			log.info("Inventory received payment event: {}", message);

			SagaEvent event = objectMapper.readValue(message, SagaEvent.class);

			if (!"PAYMENT_FAILED".equals(event.eventType())) {
				return;
			}

			/*
			 * COMPENSATING TRANSACTION
			 *
			 * Inventory was already reduced. Payment failed. So add the quantity back.
			 */
			inventoryService.releaseInventory(event.productId(), event.quantity());

			log.info("Inventory compensation completed. orderId={}", event.orderId());

			/*
			 * Inform other consumers that inventory compensation completed.
			 */
			SagaEvent releasedEvent = new SagaEvent(event.orderId(), event.productId(), event.quantity(),
					"INVENTORY_RELEASED");

			kafkaTemplate.send("inventory-topic", event.orderId().toString(),
					objectMapper.writeValueAsString(releasedEvent));

		} catch (Exception e) {

			log.error("Inventory compensation failed", e);

			throw new RuntimeException("Inventory compensation failed", e);
		}
	}
}