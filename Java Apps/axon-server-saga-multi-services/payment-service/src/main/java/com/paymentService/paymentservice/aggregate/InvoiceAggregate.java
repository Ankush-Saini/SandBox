package com.paymentService.paymentservice.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.core.apis.coreapis.commands.CreateInvoiceCommand;
import com.core.apis.coreapis.events.InvoiceCreatedEvent;
import com.paymentService.paymentservice.aggregate.types.InvoiceStatus;
@Aggregate
public class InvoiceAggregate {
	@AggregateIdentifier
	private String paymentId;
	private String orderId;
	private InvoiceStatus invoiceStatus;
	
	@CommandHandler
	public InvoiceAggregate(CreateInvoiceCommand createInvoiceCommand) {
		AggregateLifecycle.apply(new InvoiceCreatedEvent(createInvoiceCommand.paymentId, createInvoiceCommand.orderId));
	}
	
	@EventSourcingHandler
	protected void on(InvoiceCreatedEvent invoiceCreatedEvent) {
		this.paymentId=invoiceCreatedEvent.paymentId;
		this.orderId=invoiceCreatedEvent.orderId;
		this.invoiceStatus=InvoiceStatus.PAID;
	}
}
