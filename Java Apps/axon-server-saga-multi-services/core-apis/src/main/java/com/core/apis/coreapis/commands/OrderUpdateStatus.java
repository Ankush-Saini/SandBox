package com.core.apis.coreapis.commands;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderUpdateStatus {
	@TargetAggregateIdentifier
	public final String orderId;
	public final String itemType;
	public final BigDecimal price;
	public final String currency;
	public final String orderStatus;
}
