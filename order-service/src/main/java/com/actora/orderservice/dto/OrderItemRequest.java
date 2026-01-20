package com.actora.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for order item data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order item data")
public class OrderItemRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name cannot exceed 100 characters")
    @Schema(description = "Name of the product", example = "Premium Widget")
    private String productName;

    @Size(max = 50, message = "Product code cannot exceed 50 characters")
    @Schema(description = "Product code/SKU", example = "PROD-001")
    private String productCode;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Quantity ordered", example = "2")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
    @Schema(description = "Price per unit", example = "29.99")
    private BigDecimal unitPrice;
}
