package com.micro.orderservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.orderservice.store.entity.OrderEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderItemDto {
    private Long id;
    @NotNull
    @JsonProperty("product_name_code")
    @Size(message = "Product-name-code can't be less than 5 symbols", min = 5)
    private String productNameCode;
    @NotNull
    private Double price;
    @NotNull
    private Integer quantity;
}
