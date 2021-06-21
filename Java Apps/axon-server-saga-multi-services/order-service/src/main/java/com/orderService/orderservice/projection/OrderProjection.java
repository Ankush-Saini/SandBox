package com.orderService.orderservice.projection;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.core.apis.coreapis.commands.OrderUpdateStatus;
import com.core.apis.coreapis.models.response.OrderStatusQueryResponse;
import com.core.apis.coreapis.queries.OrderStatusQuery;
import com.orderService.orderservice.entities.OrderRead;
import com.orderService.orderservice.repository.OrderReadRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProjection {

	private final OrderReadRepository orderReadRepository;

	@EventHandler
	@Transactional
	public void on(OrderUpdateStatus orderUpdateStatus) {
		log.info("Updating Read DB from Write DB");
		orderReadRepository.save(new OrderRead(orderUpdateStatus.orderId, String.valueOf(orderUpdateStatus.itemType),
				orderUpdateStatus.price, orderUpdateStatus.currency, String.valueOf(orderUpdateStatus.orderStatus)));
	}

	@QueryHandler
	public OrderStatusQueryResponse handle(OrderStatusQuery orderStatusQuery) {
		OrderRead orderRead = this.orderReadRepository.findById(orderStatusQuery.getOrderId()).orElse(null);
		if (orderRead == null)
			return new OrderStatusQueryResponse();
		return new OrderStatusQueryResponse(orderRead.getOrderId(), orderRead.getItemType(), orderRead.getPrice(),
				orderRead.getCurrency(), orderRead.getOrderStatus());
	}
}
