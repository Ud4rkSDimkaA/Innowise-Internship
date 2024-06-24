package com.micro.orderservice.api.controller;

import com.micro.orderservice.api.dto.InventoryItemDto;
import com.micro.orderservice.api.dto.OrderDto;
import com.micro.orderservice.api.dto.OrderItemDto;
import com.micro.orderservice.api.exception.BadRequestException;
import com.micro.orderservice.api.exception.NotValidException;
import com.micro.orderservice.api.factory.OrderDtoFactory;
import com.micro.orderservice.store.entity.OrderEntity;
import com.micro.orderservice.store.entity.OrderItemEntity;
import com.micro.orderservice.store.repository.OrderItemRepository;
import com.micro.orderservice.store.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderDtoFactory orderDtoFactory;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final WebClient.Builder webClientBuilder;

    public static final String CREATE_ORDER = "/v1/api/orders";
    public static final String GET_ORDER_BY_ID = "/v1/api/orders/order/{order_id}";
    public static final String GET_ORDER_BY_ORDER_NUMBER = "/v1/api/orders/order/{order_number}";
    public static final String GET_ALL_ORDERS = "/v1/api/orders";

    @PostMapping(CREATE_ORDER)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public OrderDto createOrder(@Valid @RequestBody List<OrderItemDto> orderItems,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotValidException("Order item is not valid to save", bindingResult);
        }

        List<String> itemsToOrder = orderItems
                .stream()
                .map(OrderItemDto::getProductNameCode)
                .toList();

        InventoryItemDto[] inventoryItemsInStock = webClientBuilder.build().get()
                .uri("http://inventory-service/v1/api/inventory",
                        uriBuilder -> uriBuilder.
                                queryParam("productNameCode", itemsToOrder).build())
                .retrieve()
                .bodyToMono(InventoryItemDto[].class)
                .block();

        List<InventoryItemDto> inventoryItems = Arrays.stream(inventoryItemsInStock).toList();

        List<OrderItemDto> availableItemsToOrder = orderItems
                .stream()
                .filter(orderItem -> inventoryItems.stream()
                        .anyMatch(inventoryItem -> inventoryItem.getProductNameCode().equals(orderItem.getProductNameCode())))
                .toList();

        if (availableItemsToOrder.isEmpty()) {
            throw new NotValidException("Products with such names are not in stock", bindingResult);
        }

        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        OrderEntity orderEntity = OrderEntity.builder().build();
        orderEntity.setOrderNumber(UUID.randomUUID().toString());
        orderRepository.saveAndFlush(orderEntity);

        for (OrderItemDto dto : availableItemsToOrder) {
            OrderItemEntity entity = OrderItemEntity.builder()
                    .id(dto.getId())
                    .productNameCode(dto.getProductNameCode())
                    .price(dto.getPrice())
                    .quantity(dto.getQuantity())
                    .order(orderEntity)
                    .build();
            orderItemEntities.add(entity);
            orderItemRepository.saveAndFlush(entity);
        }

        orderEntity.setOrderItems(orderItemEntities);
        orderRepository.saveAndFlush(orderEntity);

        log.info("Order was placed successfully");
        return orderDtoFactory.makeOrderDto(orderEntity);
    }

    @GetMapping(GET_ORDER_BY_ID)
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    public OrderDto getOrderById(@PathVariable("order_id") Long orderId) {

        OrderEntity orderEntity = orderRepository
                .getOrderEntityById(orderId)
                .orElseThrow(() -> new BadRequestException("There is no order with such id"));

        return orderDtoFactory.makeOrderDto(orderEntity);
    }

    @GetMapping(GET_ALL_ORDERS)
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(orderDtoFactory::makeOrderDto)
                .toList();
    }

}
