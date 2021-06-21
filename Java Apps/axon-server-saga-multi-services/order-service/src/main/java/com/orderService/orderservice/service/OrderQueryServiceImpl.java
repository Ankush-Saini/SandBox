package com.orderService.orderservice.service;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.apis.coreapis.models.response.OrderStatusQueryResponse;
import com.core.apis.coreapis.queries.OrderStatusQuery;
import com.orderService.orderservice.entities.OrderWrite;

@Service
public class OrderQueryServiceImpl implements OrderQueryService{

	private QueryGateway queryGateway;
	
	public OrderQueryServiceImpl(QueryGateway queryGateway) {
		this.queryGateway=queryGateway;
	}
	
	public List<OrderWrite> getAllOrders(){
		List<OrderWrite> orders=new ArrayList<OrderWrite>();
		//orderRepository.findAll().forEach(order->orders.add(order));
		return orders;
	}
	
	//getting a specific record  
	public OrderStatusQueryResponse getOrderById(String orderId)   
	{  
		OrderStatusQueryResponse response=queryGateway.query(new OrderStatusQuery(orderId), ResponseTypes.instanceOf(OrderStatusQueryResponse.class)).join();
		return response;
	}  
	

}
