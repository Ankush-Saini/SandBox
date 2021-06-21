package com.orderService.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.apis.coreapis.models.response.OrderStatusQueryResponse;
import com.orderService.orderservice.service.OrderQueryService;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderQueryController {
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@GetMapping(value="/{orderId}")
	public OrderStatusQueryResponse getOrderById(@PathVariable String orderId) {
		return orderQueryService.getOrderById(orderId);
	}
}
