package com.core.apis.coreapis.events;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCreatedEvent {
	  public final String orderId;
	  public final String itemType;
	  public final BigDecimal price;
	  public final String currency;
	  public final String orderStatus;
}
