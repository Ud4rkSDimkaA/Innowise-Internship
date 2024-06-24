package com.micro.productservice.api.controller;


import com.micro.productservice.api.dto.InventoryItemDto;
import com.micro.productservice.api.dto.ProductDto;
import com.micro.productservice.api.exception.BadRequestException;
import com.micro.productservice.api.exception.NotValidException;
import com.micro.productservice.api.factory.ProductDtoFactory;
import com.micro.productservice.store.entity.ProductEntity;
import com.micro.productservice.store.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductDtoFactory productDtoFactory;
    private final ProductRepository productRepository;
    private final WebClient.Builder webClientBuilder;

    public static final String CREATE_PRODUCT = "/v1/api/products/product";
    public static final String GET_ALL_PRODUCTS = "/v1/api/products";
    public static final String UPDATE_PRODUCT = "/v1/api/products/product/{product_name}";
    public static final String GET_PRODUCT_BY_NAME = "/v1/api/products/product/{product_name}";
    public static final String DELETE_PRODUCT_BY_NAME = "/v1/api/products/product/{product_name}";

    @PostMapping(CREATE_PRODUCT)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new NotValidException("Product is not valid to save", bindingResult);
        }

        productRepository.getProductEntityByName(productDto.getName())
                .ifPresent(product -> {
                    throw new BadRequestException("Product [" + product.getName() + "] already exists");
                });


        ProductEntity productEntity = productRepository.saveAndFlush(
                ProductEntity.builder()
                        .name(productDto.getName())
                        .description(productDto.getDescription())
                        .price(productDto.getPrice())
                        .quantity(productDto.getQuantity())
                        .createdAt(productDto.getCreatedAt())
                        .updatedAt(productDto.getUpdatedAt())
                        .build()
        );

        InventoryItemDto inventoryItem = InventoryItemDto.builder()
                .productNameCode(productDto.getName())
                .quantity(productDto.getQuantity())
                .build();

        webClientBuilder.build().post()
                .uri("http://inventory-service/v1/api/inventory")
                        .body(Mono.just(inventoryItem), InventoryItemDto.class)
                                .retrieve()
                                        .bodyToMono(Void.class)
                                                .subscribe();




        log.info("Product [" + productDto.getName() + "] was saved to Database");
        return productDtoFactory.makeProductDto(productEntity);
    }

    @GetMapping(GET_ALL_PRODUCTS)
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(productDtoFactory::makeProductDto)
                .toList();
    }

    @GetMapping(GET_PRODUCT_BY_NAME)
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    public ProductDto getProductByName(@PathVariable("product_name") String productName){
        ProductEntity productEntity = productRepository.getProductEntityByName(productName)
                .orElseThrow(() -> new BadRequestException("There is no product with such name"));

        return productDtoFactory.makeProductDto(productEntity);
    }


    @PutMapping(UPDATE_PRODUCT)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ProductDto updateProductName(
            @PathVariable("product_name") String productName,
            @Valid @RequestBody ProductDto updatedProduct,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotValidException("Product is not valid to update", bindingResult);
        }

        ProductEntity entityToUpdate = productRepository.getProductEntityByName(productName)
                .orElseThrow(() -> new BadRequestException("There is no product with such name: " + productName));

        entityToUpdate.setName(updatedProduct.getName());
        entityToUpdate.setPrice(updatedProduct.getPrice());
        entityToUpdate.setDescription(updatedProduct.getDescription());
        entityToUpdate.setQuantity(updatedProduct.getQuantity());
        entityToUpdate.setCreatedAt(updatedProduct.getCreatedAt());
        entityToUpdate.setUpdatedAt(updatedProduct.getUpdatedAt());

        ProductEntity productEntity = productRepository.saveAndFlush(entityToUpdate);
        log.info("Product [" + productName + "] was updated to [" + updatedProduct.getName() + "] and saved");
        return productDtoFactory.makeProductDto(productEntity);
    }

    @DeleteMapping(DELETE_PRODUCT_BY_NAME)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void deleteProductByName(@PathVariable("product_name") String productName){
        ProductEntity productEntityToDelete = productRepository.getProductEntityByName(productName)
                .orElseThrow(() -> new BadRequestException("There is no product with such name: " + productName));

        productRepository.delete(productEntityToDelete);
        log.info("Product [" + productName + "] was successfully deleted");
    }
}
