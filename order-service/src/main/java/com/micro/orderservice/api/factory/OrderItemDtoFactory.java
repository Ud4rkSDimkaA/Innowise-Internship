package com.micro.orderservice.api.factory;

import com.micro.orderservice.api.dto.OrderItemDto;
import com.micro.orderservice.store.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDtoFactory {

    public OrderItemDto makeOrderItemDto(OrderItemEntity entity){
        return OrderItemDto.builder()
                .id(entity.getId())
                .price(entity.getPrice())
                .productNameCode(entity.getProductNameCode())
                .quantity(entity.getQuantity())
                .build();
    }

}
