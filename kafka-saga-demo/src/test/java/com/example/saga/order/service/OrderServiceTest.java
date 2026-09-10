package com.example.saga.order.service;

import com.example.saga.order.entity.Order;
import com.example.saga.order.repository.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	@InjectMocks
	private OrderService orderService;

	private Order savedOrder;

	@BeforeEach
	void setUp() {

		savedOrder = new Order();

		savedOrder.setId(1L);
		savedOrder.setProductId(100L);
		savedOrder.setQuantity(2);
		savedOrder.setStatus("CREATED");
	}

	@Test
	void shouldCreateOrderSuccessfully() {

		when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

		when(kafkaTemplate.send(anyString(), anyString(), anyString()))
				.thenReturn(CompletableFuture.completedFuture(null));

		Order result = orderService.createOrder(100L, 2);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(100L, result.getProductId());
		assertEquals(2, result.getQuantity());
		assertEquals("CREATED", result.getStatus());

		verify(orderRepository, times(1)).save(any(Order.class));

		verify(kafkaTemplate, times(1)).send(eq("order-topic"), eq("1"), anyString());
	}

	@Test
	void shouldPublishOrderCreatedEvent() {

		when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

		when(kafkaTemplate.send(anyString(), anyString(), anyString()))
				.thenReturn(CompletableFuture.completedFuture(null));

		orderService.createOrder(100L, 2);

		ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

		verify(kafkaTemplate).send(eq("order-topic"), eq("1"), messageCaptor.capture());

		String message = messageCaptor.getValue();

		assertTrue(message.contains("\"orderId\":1"));
		assertTrue(message.contains("\"productId\":100"));
		assertTrue(message.contains("\"quantity\":2"));
		assertTrue(message.contains("\"eventType\":\"ORDER_CREATED\""));
	}

	@Test
	void shouldThrowExceptionWhenOrderSaveFails() {

		when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> orderService.createOrder(100L, 2));

		verify(orderRepository, times(1)).save(any(Order.class));

		verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
	}

	@Test
	void shouldThrowExceptionWhenKafkaSendFails() {

		when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

		when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenThrow(new RuntimeException("Kafka error"));

		assertThrows(RuntimeException.class, () -> orderService.createOrder(100L, 2));

		verify(orderRepository, times(1)).save(any(Order.class));

		verify(kafkaTemplate, times(1)).send(eq("order-topic"), eq("1"), anyString());
	}
}