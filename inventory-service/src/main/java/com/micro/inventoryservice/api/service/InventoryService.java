package com.micro.inventoryservice.api.service;

import com.micro.inventoryservice.store.entity.InventoryEntity;
import com.micro.inventoryservice.store.store.InventoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private static InventoryEntity inventoryInstance;
    private final InventoryRepository inventoryRepository;

    @PostConstruct
    @Transactional
    public void init() {
        Optional<InventoryEntity> inventory = inventoryRepository.findFirst();
        if (inventory.isPresent()) {
            inventoryInstance = inventory.get();
        } else {
            inventoryInstance = InventoryEntity.builder().build();
            inventoryRepository.saveAndFlush(inventoryInstance);
        }
    }

    public InventoryEntity getInventory() {
        return inventoryInstance;
    }
}
