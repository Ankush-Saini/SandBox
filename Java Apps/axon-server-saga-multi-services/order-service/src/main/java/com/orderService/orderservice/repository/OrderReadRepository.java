package com.orderService.orderservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.orderService.orderservice.entities.OrderRead;

public interface OrderReadRepository  extends MongoRepository<OrderRead, String>{

}
