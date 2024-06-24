package com.micro.productservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDto {
    private Long id;
    @NotNull(message = "Name can't be empty")
    @Size(min = 3, message = "Name of product has to be more than 3 symbols")
    private String name;
    @NotNull(message = "Description can't be empty")
    @Size(min = 3, message = "Description of product has to be more than 3 symbols")
    private String description;
    @NotNull(message = "Price can't be empty")
    private Double price;
    @NotNull(message = "Quantity can't be empty")
    private Integer quantity;
    @JsonProperty("created_at")
    private Instant createdAt = Instant.now();
    @JsonProperty("updated_at")
    private Instant updatedAt = Instant.now();
}
