package com.orderService.orderservice.sagas;

import java.util.UUID;

import javax.inject.Inject;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

import com.core.apis.coreapis.commands.CreateInvoiceCommand;
import com.core.apis.coreapis.commands.CreateShippingCommand;
import com.core.apis.coreapis.commands.UpdateOrderStatusCommand;
import com.core.apis.coreapis.events.InvoiceCreatedEvent;
import com.core.apis.coreapis.events.OrderCreatedEvent;
import com.core.apis.coreapis.events.OrderShippedEvent;
import com.core.apis.coreapis.events.OrderUpdatedEvent;
import com.orderService.orderservice.aggregates.types.OrderStatus;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class OrderManagementSaga {
	@Inject
	private transient CommandGateway commandGateway;
	
	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		String paymentId=UUID.randomUUID().toString();
		
		//associate Saga
		SagaLifecycle.associateWith("paymentId",paymentId);
		
		log.info("Order Id: "+orderCreatedEvent.orderId);
		
		commandGateway.send(new CreateInvoiceCommand(paymentId, orderCreatedEvent.orderId));
	}
	
	@SagaEventHandler(associationProperty = "paymentId")
	public void handle(InvoiceCreatedEvent invoiceCreatedEvent) {
		String shipmentId=UUID.randomUUID().toString();
		log.info("Saga Continued using Payment ID for order Id: "+invoiceCreatedEvent.orderId);
		log.info("PaymentId "+invoiceCreatedEvent.paymentId);
		log.info("ShippingId "+shipmentId);
		//log.info(new CreateShippingCommand(shipmentId, invoiceCreatedEvent.orderId,invoiceCreatedEvent.paymentId ).toString());
		SagaLifecycle.associateWith("shipping",shipmentId);
		commandGateway.send(new CreateShippingCommand(shipmentId, invoiceCreatedEvent.orderId,invoiceCreatedEvent.paymentId ));
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderShippedEvent orderShippedEvent) {
		log.info("Order Shipped: "+orderShippedEvent.orderId);
		commandGateway.send(new UpdateOrderStatusCommand(orderShippedEvent.orderId,String.valueOf(OrderStatus.SHIPPED)));
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderUpdatedEvent orderUpdatedEvent) {
		log.info("Order Updated: "+orderUpdatedEvent.orderId);
		SagaLifecycle.end();
	}
}
