package com.core.apis.coreapis.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateInvoiceCommand {
	@TargetAggregateIdentifier
    public final String paymentId;
    public final String orderId;
}
