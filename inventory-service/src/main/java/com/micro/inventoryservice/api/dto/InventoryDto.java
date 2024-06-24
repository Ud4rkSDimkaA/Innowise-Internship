package com.micro.inventoryservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InventoryDto {
    private Long id;
    @JsonProperty("inventory_items")
    private List<InventoryItemDto> inventoryItems;
}
