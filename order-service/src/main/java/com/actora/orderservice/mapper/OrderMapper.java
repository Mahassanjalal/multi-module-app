package com.actora.orderservice.mapper;

import com.actora.orderservice.dto.*;
import com.actora.orderservice.entity.Order;
import com.actora.orderservice.entity.OrderItem;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Order entity and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    /**
     * Convert CreateOrderRequest to Order entity (without items).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(CreateOrderRequest request);

    /**
     * Convert OrderItemRequest to OrderItem entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    OrderItem toEntity(OrderItemRequest request);

    /**
     * Convert list of OrderItemRequest to list of OrderItem entities.
     */
    List<OrderItem> toItemEntities(List<OrderItemRequest> requests);

    /**
     * Convert Order entity to OrderResponse DTO.
     */
    @Mapping(target = "user", ignore = true)
    OrderResponse toResponse(Order order);

    /**
     * Convert OrderItem entity to OrderItemResponse DTO.
     */
    OrderItemResponse toItemResponse(OrderItem item);

    /**
     * Convert list of Order entities to list of OrderResponse DTOs.
     */
    List<OrderResponse> toResponseList(List<Order> orders);

    /**
     * Update Order entity from UpdateOrderRequest DTO.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateOrderRequest request, @MappingTarget Order order);
}
