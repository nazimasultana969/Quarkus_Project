package com.example.saga.common;

public record SagaEvent(

		Long orderId,

		Long productId,

		Integer quantity,

		String eventType

) {
}