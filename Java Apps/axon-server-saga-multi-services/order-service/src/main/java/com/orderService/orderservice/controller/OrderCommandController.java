package com.orderService.orderservice.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orderService.orderservice.dto.OrderCreateDTO;
import com.orderService.orderservice.service.OrderCommandService;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderCommandController {
	@Autowired
	private OrderCommandService orderCommandService;
	
	@PostMapping
	public CompletableFuture<String> createOrder(@RequestBody OrderCreateDTO createDTO){
		return orderCommandService.createOrder(createDTO);
	}
}
