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

    private static final Logger log =
            LoggerFactory.getLogger(PaymentConsumer.class);

    private final PaymentService paymentService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public PaymentConsumer(
            PaymentService paymentService,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {

        this.paymentService = paymentService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "inventory-topic",
            groupId = "payment-group")
    public void consumeInventory(String message) {

        try {

            log.info(
                    "Payment received inventory event: {}",
                    message);

            SagaEvent event =
                    objectMapper.readValue(
                            message,
                            SagaEvent.class);

            if (!"INVENTORY_RESERVED"
                    .equals(event.eventType())) {

                return;
            }

            /*
             * Process payment.
             */
            boolean paymentSuccess =
                    paymentService.processPayment(
                            event.orderId());

            String eventType;

            if (paymentSuccess) {

                eventType = "PAYMENT_SUCCESS";

                log.info(
                        "Payment successful. orderId={}",
                        event.orderId());

            } else {

                eventType = "PAYMENT_FAILED";

                log.warn(
                        "Payment failed. orderId={}",
                        event.orderId());
            }

            /*
             * Publish payment result.
             *
             * Both OrderConsumer and
             * InventoryConsumer listen to payment-topic.
             */
            SagaEvent paymentEvent =
                    new SagaEvent(
                            event.orderId(),
                            event.productId(),
                            event.quantity(),
                            eventType
                    );

            kafkaTemplate.send(
                    "payment-topic",
                    event.orderId().toString(),
                    objectMapper.writeValueAsString(
                            paymentEvent));

            log.info(
                    "{} event published. orderId={}",
                    eventType,
                    event.orderId());

        } catch (Exception e) {

            log.error(
                    "Payment consumer failed",
                    e);

            throw new RuntimeException(
                    "Payment consumer failed", e);
        }
    }
}