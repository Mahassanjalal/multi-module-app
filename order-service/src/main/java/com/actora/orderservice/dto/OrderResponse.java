package com.actora.orderservice.dto;

import com.actora.orderservice.entity.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for order response data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Order response data")
public class OrderResponse {

    @Schema(description = "Order's unique identifier", example = "1")
    private Long id;

    @Schema(description = "Order number", example = "ORD-20240115-001")
    private String orderNumber;

    @Schema(description = "User ID who placed the order", example = "1")
    private Long userId;

    @Schema(description = "User information (if available)")
    private UserInfo user;

    @Schema(description = "Order description", example = "Monthly subscription order")
    private String description;

    @Schema(description = "Order status", example = "PENDING")
    private Order.OrderStatus status;

    @Schema(description = "Total order amount", example = "99.99")
    private BigDecimal totalAmount;

    @Schema(description = "Shipping address", example = "123 Main St, City, Country")
    private String shippingAddress;

    @Schema(description = "List of order items")
    private List<OrderItemResponse> items;

    @Schema(description = "Order creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Order last update timestamp")
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Basic user information")
    public static class UserInfo {
        private Long id;
        private String fullName;
        private String email;
    }
}
