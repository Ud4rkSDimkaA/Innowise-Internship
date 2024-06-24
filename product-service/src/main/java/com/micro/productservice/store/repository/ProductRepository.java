package com.micro.productservice.store.repository;

import com.micro.productservice.store.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> getProductEntityByName(String name);
}
