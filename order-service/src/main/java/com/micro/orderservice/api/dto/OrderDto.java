package com.micro.orderservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.orderservice.store.entity.OrderItemEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDto {
    private Long id;
    private String orderNumber;
    @JsonProperty("order_items")
    private List<OrderItemDto> orderItems;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("updated_at")
    private Instant updatedAt;
}
