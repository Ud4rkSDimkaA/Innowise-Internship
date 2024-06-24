package com.micro.inventoryservice.store.store;

import com.micro.inventoryservice.store.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    @Query("SELECT DISTINCT i FROM InventoryEntity i LEFT JOIN FETCH i.inventoryItems")
    Optional<InventoryEntity> findFirst();


}
