package com.micro.inventoryservice.api.factory;

import com.micro.inventoryservice.api.dto.InventoryDto;
import com.micro.inventoryservice.api.dto.InventoryItemDto;
import com.micro.inventoryservice.store.entity.InventoryEntity;
import com.micro.inventoryservice.store.entity.InventoryItemEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryItemDtoFactory {
    public InventoryItemDto makeInventoryItemDto(InventoryItemEntity entity){
        return InventoryItemDto.builder()
                .id(entity.getId())
                .productNameCode(entity.getProductNameCode())
                .quantity(entity.getQuantity())
                .build();
    }

}
