package com.orderService.orderservice.service;

import java.util.List;

import com.core.apis.coreapis.models.response.OrderStatusQueryResponse;
import com.orderService.orderservice.entities.OrderWrite;

public interface OrderQueryService {
	public List<OrderWrite> getAllOrders();
	public OrderStatusQueryResponse getOrderById(String order);
}
