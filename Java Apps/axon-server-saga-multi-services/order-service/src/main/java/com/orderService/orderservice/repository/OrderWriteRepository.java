package com.orderService.orderservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.orderService.orderservice.entities.OrderWrite;


public interface OrderWriteRepository extends MongoRepository<OrderWrite, String>{

}
