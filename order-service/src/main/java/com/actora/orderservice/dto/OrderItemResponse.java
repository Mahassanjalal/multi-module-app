package com.actora.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for order item response data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Order item response data")
public class OrderItemResponse {

    @Schema(description = "Item's unique identifier", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "Premium Widget")
    private String productName;

    @Schema(description = "Product code", example = "PROD-001")
    private String productCode;

    @Schema(description = "Quantity", example = "2")
    private Integer quantity;

    @Schema(description = "Unit price", example = "29.99")
    private BigDecimal unitPrice;

    @Schema(description = "Total price for this item", example = "59.98")
    private BigDecimal totalPrice;
}
