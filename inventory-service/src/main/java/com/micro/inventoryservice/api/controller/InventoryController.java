package com.micro.inventoryservice.api.controller;

import com.micro.inventoryservice.api.dto.InventoryDto;
import com.micro.inventoryservice.api.dto.InventoryItemDto;
import com.micro.inventoryservice.api.factory.InventoryDtoFactory;
import com.micro.inventoryservice.api.factory.InventoryItemDtoFactory;
import com.micro.inventoryservice.api.service.InventoryService;
import com.micro.inventoryservice.store.entity.InventoryEntity;
import com.micro.inventoryservice.store.entity.InventoryItemEntity;
import com.micro.inventoryservice.store.store.InventoryItemRepository;
import com.micro.inventoryservice.store.store.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryItemDtoFactory inventoryItemDtoFactory;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryRepository inventoryRepository;

    private final InventoryService inventoryService;
    private final InventoryDtoFactory inventoryDtoFactory;

    public static final String GET_INVENTORY = "/v1/api/inventory/all";
    public static final String IS_IN_STOCK = "/v1/api/inventory";
    public static final String ADD_ITEM_TO_INVENTORY = "/v1/api/inventory";

    @GetMapping(GET_INVENTORY)
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    public InventoryDto getInventoryItems() {
        return inventoryDtoFactory
                .makeInventoryDto(inventoryService.getInventory());
    }

    @GetMapping(IS_IN_STOCK)
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    public List<InventoryItemDto> getItemInStock(@RequestParam List<String> productNameCode) {

        return inventoryService
                .getInventory()
                .getInventoryItems()
                .stream()
                .filter(item -> productNameCode.contains(item.getProductNameCode()))
                .filter(item -> item.getQuantity() > 0)
                .map(inventoryItemDtoFactory::makeInventoryItemDto)
                .toList();
    }

    @PostMapping(ADD_ITEM_TO_INVENTORY)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void addItemToInventory(@RequestBody InventoryItemDto inventoryItemDto) {
        InventoryEntity inventory = inventoryService.getInventory();

        boolean isAlreadyExist = inventoryService
                .getInventory()
                .getInventoryItems()
                .stream()
                .anyMatch(item -> item.getProductNameCode().equals(inventoryItemDto.getProductNameCode()));

        if (!isAlreadyExist) {
            InventoryItemEntity build = InventoryItemEntity.builder()
                    .productNameCode(inventoryItemDto.getProductNameCode())
                    .quantity(inventoryItemDto.getQuantity())
                    .inventory(inventory)
                    .build();
            inventoryItemRepository.saveAndFlush(build);

            inventory.setInventoryItems(List.of(build));
            inventoryRepository.saveAndFlush(inventory);
        }

    }
}
