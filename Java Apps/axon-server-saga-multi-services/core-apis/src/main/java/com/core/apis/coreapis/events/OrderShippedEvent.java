package com.core.apis.coreapis.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderShippedEvent {
	public final String shippingId;
    public final String orderId;
    public final String paymentId;
}
