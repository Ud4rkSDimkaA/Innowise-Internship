package com.micro.gatewayservice.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                //Routes for Inventory-service
                .route("inventory-all", r -> r.path("/v1/api/inventory/all")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://inventory-service"))
                .route("inventory-is-in-stock", r -> r.path("/v1/api/inventory")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://inventory-service"))
                .route("inventory-add-item", r -> r.path("/v1/api/inventory")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://inventory-service"))

                //Routes for Order-service
                .route("order-create", r -> r.path("/v1/api/orders")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://order-service"))
                .route("order-get-by-id", r -> r.path("/v1/api/orders/order/{order_id}")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://order-service"))
                .route("order-get-all", r -> r.path("/v1/api/orders")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://order-service"))

                //Routes for Product-service
                .route("product-create", r -> r.path("/v1/api/products/product")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://product-service"))
                .route("product-get-all", r -> r.path("/v1/api/products")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://product-service"))
                .route("product-update", r -> r.path("/v1/api/products/product/{product_name}")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://product-service"))
                .route("product-get-by-name", r -> r.path("/v1/api/products/product/{product_name}")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://product-service"))
                .route("product-delete-by-name", r -> r.path("/v1/api/products/product/{product_name}")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://product-service"))
                .route("eureka", r-> r.path("eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(5, 10);
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
