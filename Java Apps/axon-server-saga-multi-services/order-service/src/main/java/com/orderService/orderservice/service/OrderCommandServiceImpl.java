package com.orderService.orderservice.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import com.core.apis.coreapis.commands.CreateOrderCommand;
import com.orderService.orderservice.aggregates.types.OrderStatus;
import com.orderService.orderservice.dto.OrderCreateDTO;

@Service
public class OrderCommandServiceImpl implements OrderCommandService{
	private final CommandGateway commandGateway;
	
	public OrderCommandServiceImpl(CommandGateway commandGateway) {
		this.commandGateway=commandGateway;
	}
	public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO) {
		return commandGateway.send(new CreateOrderCommand(UUID.randomUUID().toString(), orderCreateDTO.getItemType(),
                orderCreateDTO.getPrice(), orderCreateDTO.getCurrency(), String.valueOf(OrderStatus.CREATED)));
	}
	
}
