package com.core.apis.coreapis.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateShippingCommand {
	 @TargetAggregateIdentifier
	 public final String shippingId;
	 public final String orderId;
	 public final String paymentId;
	 
}
