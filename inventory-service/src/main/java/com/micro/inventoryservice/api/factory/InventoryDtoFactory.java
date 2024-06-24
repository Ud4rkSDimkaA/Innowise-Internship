package com.micro.inventoryservice.api.factory;

import com.micro.inventoryservice.api.dto.InventoryDto;
import com.micro.inventoryservice.store.entity.InventoryEntity;
import com.micro.inventoryservice.store.store.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryDtoFactory {

    private final InventoryItemDtoFactory inventoryItemDtoFactory;

    public InventoryDto makeInventoryDto(InventoryEntity entity) {
        return InventoryDto.builder()
                .id(entity.getId())
                .inventoryItems(
                        entity
                        .getInventoryItems()
                        .stream()
                        .map(inventoryItemDtoFactory::makeInventoryItemDto)
                        .toList()
                )
                .build();
    }

}
