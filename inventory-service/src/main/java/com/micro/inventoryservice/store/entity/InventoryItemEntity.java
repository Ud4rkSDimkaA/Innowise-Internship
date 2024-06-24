package com.micro.inventoryservice.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InventoryItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productNameCode;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private InventoryEntity inventory;
}
