package com.micro.orderservice.api.factory;

import com.micro.orderservice.api.dto.OrderDto;
import com.micro.orderservice.store.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDtoFactory {

    private final OrderItemDtoFactory orderItemDtoFactory;

    public OrderDto makeOrderDto(OrderEntity entity){
        return OrderDto.builder()
                .id(entity.getId())
                .orderNumber(entity.getOrderNumber())
                .updatedAt(entity.getUpdatedAt())
                .createdAt(entity.getCreatedAt())
                .orderItems(entity
                        .getOrderItems()
                        .stream()
                        .map(orderItemDtoFactory::makeOrderItemDto)
                        .toList()
                )
                .build();
    }
}
