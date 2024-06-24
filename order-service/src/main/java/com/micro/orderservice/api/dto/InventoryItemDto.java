package com.micro.orderservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class InventoryItemDto {
    private Long id;
    @NotNull
    @JsonProperty("product_name_code")
    @Size(message = "Product-name-code can't be less than 5 symbols", min = 5)
    private String productNameCode;
    @NotNull
    private Integer quantity;
}
