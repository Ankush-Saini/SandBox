package com.shippingService.shippingservice.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.core.apis.coreapis.commands.CreateShippingCommand;
import com.core.apis.coreapis.events.OrderShippedEvent;

@Aggregate
public class ShippingAggregate {
	@AggregateIdentifier
	private String shippingId;
	private String orderId;
	private String paymentId;
	
	@CommandHandler
	public ShippingAggregate(CreateShippingCommand createShippingCommand) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AggregateLifecycle.apply(new OrderShippedEvent(createShippingCommand.shippingId, createShippingCommand.orderId, createShippingCommand.paymentId));
	}
	
	@EventSourcingHandler
	protected void on(OrderShippedEvent orderShippedEvent) {
		this.shippingId=orderShippedEvent.shippingId;
		this.orderId=orderShippedEvent.orderId;
		this.paymentId=orderShippedEvent.paymentId;
	}
}
