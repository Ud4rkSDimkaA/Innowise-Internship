package com.micro.inventoryservice.store.store;

import com.micro.inventoryservice.store.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Long> {
    Optional<InventoryItemEntity> getInventoryItemEntityByProductNameCode(String productNameCode);
}
