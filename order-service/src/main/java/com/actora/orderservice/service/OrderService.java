package com.actora.orderservice.service;

import com.actora.orderservice.dto.*;
import com.actora.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for Order operations.
 */
public interface OrderService {

    /**
     * Create a new order.
     */
    OrderResponse createOrder(CreateOrderRequest request);

    /**
     * Get an order by ID.
     */
    OrderResponse getOrderById(Long id);

    /**
     * Get an order by order number.
     */
    OrderResponse getOrderByOrderNumber(String orderNumber);

    /**
     * Get all orders with pagination.
     */
    Page<OrderResponse> getAllOrders(Pageable pageable);

    /**
     * Get all orders for a specific user.
     */
    Page<OrderResponse> getOrdersByUserId(Long userId, Pageable pageable);

    /**
     * Get all orders by status.
     */
    List<OrderResponse> getOrdersByStatus(Order.OrderStatus status);

    /**
     * Search orders by various criteria.
     */
    Page<OrderResponse> searchOrders(String searchTerm, Order.OrderStatus status, Long userId, Pageable pageable);

    /**
     * Update an existing order.
     */
    OrderResponse updateOrder(Long id, UpdateOrderRequest request);

    /**
     * Update order status.
     */
    OrderResponse updateOrderStatus(Long id, Order.OrderStatus status);

    /**
     * Cancel an order.
     */
    OrderResponse cancelOrder(Long id);

    /**
     * Delete an order by ID.
     */
    void deleteOrder(Long id);

    /**
     * Get order statistics.
     */
    OrderStatistics getOrderStatistics();

    /**
     * Order statistics DTO.
     */
    record OrderStatistics(
        long totalOrders,
        long pendingOrders,
        long confirmedOrders,
        long processingOrders,
        long shippedOrders,
        long deliveredOrders,
        long cancelledOrders
    ) {}
}
