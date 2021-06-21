package com.orderService.orderservice.service;

import java.util.concurrent.CompletableFuture;

import com.orderService.orderservice.dto.OrderCreateDTO;

public interface OrderCommandService {
	public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO);
}
