package com.orderService.orderservice.aggregates;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

import com.core.apis.coreapis.commands.CreateOrderCommand;
import com.core.apis.coreapis.commands.OrderUpdateStatus;
import com.core.apis.coreapis.commands.UpdateOrderStatusCommand;
import com.core.apis.coreapis.events.OrderCreatedEvent;
import com.core.apis.coreapis.events.OrderUpdatedEvent;
import com.orderService.orderservice.aggregates.types.ItemType;
import com.orderService.orderservice.aggregates.types.OrderStatus;
import com.orderService.orderservice.entities.OrderWrite;
import com.orderService.orderservice.repository.OrderWriteRepository;
import com.orderService.orderservice.service.OrderCommandService;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Aggregate
public class OrderAggregate {
	@AggregateIdentifier
    private String orderId;
    private ItemType itemType;
    private BigDecimal price;
    private String currency;
    private OrderStatus orderStatus;
    
    @CommandHandler
    public OrderAggregate(CreateOrderCommand orderCommand) {
    	OrderCreatedEvent orderCreatedEvent=new OrderCreatedEvent(orderCommand.orderId,orderCommand.itemType,orderCommand.price,orderCommand.currency,orderCommand.orderStatus);
    	AggregateLifecycle.apply(orderCreatedEvent);
    }
    
    @EventSourcingHandler
    protected void on(OrderCreatedEvent orderCreatedEvent,OrderWriteRepository orderWriteRepository){
        this.orderId = orderCreatedEvent.orderId;
        this.itemType = ItemType.valueOf(orderCreatedEvent.itemType);
        this.price = orderCreatedEvent.price;
        this.currency = orderCreatedEvent.currency;
        this.orderStatus = OrderStatus.valueOf(orderCreatedEvent.orderStatus);
        orderWriteRepository.save(new OrderWrite(this.orderId,String.valueOf(this.itemType),this.price,this.currency,String.valueOf(this.orderStatus)));
    }
    
    @CommandHandler
    protected void on(UpdateOrderStatusCommand updateOrderStatusCommand){
        AggregateLifecycle.apply(new OrderUpdatedEvent(updateOrderStatusCommand.orderId, updateOrderStatusCommand.orderStatus));
    }

    @EventSourcingHandler
    protected void on(OrderUpdatedEvent orderUpdatedEvent,OrderWriteRepository orderWriteRepository){
        this.orderId = orderId;
        this.orderStatus = OrderStatus.valueOf(orderUpdatedEvent.orderStatus);
        orderWriteRepository.save(new OrderWrite(this.orderId,String.valueOf(this.itemType),this.price,this.currency,String.valueOf(this.orderStatus)));
        AggregateLifecycle.apply(new OrderUpdateStatus(this.orderId, String.valueOf(this.itemType), this.price, this.currency, String.valueOf(this.orderStatus)));
    }
}
