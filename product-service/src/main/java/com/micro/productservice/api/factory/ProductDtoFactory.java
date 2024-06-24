package com.micro.productservice.api.factory;


import com.micro.productservice.api.dto.ProductDto;
import com.micro.productservice.store.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoFactory {
    public ProductDto makeProductDto(ProductEntity entity){
        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
