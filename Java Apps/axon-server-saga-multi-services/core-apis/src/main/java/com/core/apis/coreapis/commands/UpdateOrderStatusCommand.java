package com.core.apis.coreapis.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateOrderStatusCommand {
	 @TargetAggregateIdentifier
	 public final String orderId;
	 public final String orderStatus;

}
