package com.example.saga.order.controller;

import com.example.saga.order.entity.Order;
import com.example.saga.order.service.OrderService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestParam @NotNull Long productId,
			@RequestParam @Min(1) Integer quantity) {

		Order order = orderService.createOrder(productId, quantity);

		return ResponseEntity.ok(order);
	}
}