package com.core.apis.coreapis.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderUpdatedEvent {
	 public final String orderId;
	 public final String orderStatus;
}
