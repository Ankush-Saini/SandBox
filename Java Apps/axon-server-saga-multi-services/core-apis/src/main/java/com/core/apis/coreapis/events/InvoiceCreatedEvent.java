package com.core.apis.coreapis.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceCreatedEvent {
	public final String paymentId;
    public final String orderId;
}
