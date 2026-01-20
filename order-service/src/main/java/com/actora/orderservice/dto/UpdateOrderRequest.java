package com.actora.orderservice.dto;

import com.actora.orderservice.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for updating an existing order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for updating an existing order")
public class UpdateOrderRequest {

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Order description", example = "Updated order description")
    private String description;

    @Size(max = 200, message = "Shipping address cannot exceed 200 characters")
    @Schema(description = "Shipping address", example = "456 New St, City, Country")
    private String shippingAddress;

    @Schema(description = "Order status", example = "PROCESSING")
    private Order.OrderStatus status;

    @Valid
    @Schema(description = "Updated list of items (optional)")
    private List<OrderItemRequest> items;
}
