package com.micro.orderservice.store.repository;

import com.micro.orderservice.store.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> getOrderEntityById(Long id);
}
