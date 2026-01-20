package com.actora.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating a new order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating a new order")
public class CreateOrderRequest {

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user placing the order", example = "1")
    private Long userId;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Order description", example = "Monthly subscription order")
    private String description;

    @Size(max = 200, message = "Shipping address cannot exceed 200 characters")
    @Schema(description = "Shipping address", example = "123 Main St, City, Country")
    private String shippingAddress;

    @NotEmpty(message = "Order must have at least one item")
    @Valid
    @Schema(description = "List of items in the order")
    private List<OrderItemRequest> items;
}
