package com.actora.orderservice.service.impl;

import com.actora.common.dto.ApiResponse;
import com.actora.common.exception.BusinessException;
import com.actora.common.exception.ResourceNotFoundException;
import com.actora.orderservice.client.UserClient;
import com.actora.orderservice.dto.*;
import com.actora.orderservice.entity.Order;
import com.actora.orderservice.entity.OrderItem;
import com.actora.orderservice.mapper.OrderMapper;
import com.actora.orderservice.repository.OrderRepository;
import com.actora.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of OrderService with business logic for order management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserClient userClient;

    private static final AtomicLong orderCounter = new AtomicLong(0);

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating new order for user ID: {}", request.getUserId());

        // Validate user exists (with circuit breaker fallback)
        validateUserExists(request.getUserId());

        // Create order entity
        Order order = orderMapper.toEntity(request);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(Order.OrderStatus.PENDING);

        // Add items and calculate total
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            OrderItem item = orderMapper.toEntity(itemRequest);
            BigDecimal itemTotal = itemRequest.getUnitPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            item.setTotalPrice(itemTotal);
            totalAmount = totalAmount.add(itemTotal);
            order.addItem(item);
        }
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with number: {}", savedOrder.getOrderNumber());

        return enrichWithUserInfo(orderMapper.toResponse(savedOrder));
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        log.debug("Fetching order with ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        return enrichWithUserInfo(orderMapper.toResponse(order));
    }

    @Override
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        log.debug("Fetching order with number: {}", orderNumber);

        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        return enrichWithUserInfo(orderMapper.toResponse(order));
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        log.debug("Fetching all orders with pagination: {}", pageable);

        return orderRepository.findAll(pageable)
                .map(order -> enrichWithUserInfo(orderMapper.toResponse(order)));
    }

    @Override
    public Page<OrderResponse> getOrdersByUserId(Long userId, Pageable pageable) {
        log.debug("Fetching orders for user ID: {}", userId);

        return orderRepository.findByUserId(userId, pageable)
                .map(order -> enrichWithUserInfo(orderMapper.toResponse(order)));
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(Order.OrderStatus status) {
        log.debug("Fetching orders with status: {}", status);

        return orderRepository.findByStatus(status).stream()
                .map(order -> enrichWithUserInfo(orderMapper.toResponse(order)))
                .toList();
    }

    @Override
    public Page<OrderResponse> searchOrders(String searchTerm, Order.OrderStatus status, Long userId, Pageable pageable) {
        log.debug("Searching orders with term: '{}', status: {}, userId: {}", searchTerm, status, userId);

        return orderRepository.searchOrders(searchTerm, status, userId, pageable)
                .map(order -> enrichWithUserInfo(orderMapper.toResponse(order)));
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, UpdateOrderRequest request) {
        log.info("Updating order with ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        // Check if order can be updated
        if (order.getStatus() == Order.OrderStatus.DELIVERED ||
            order.getStatus() == Order.OrderStatus.CANCELLED ||
            order.getStatus() == Order.OrderStatus.REFUNDED) {
            throw new BusinessException("Cannot update order in " + order.getStatus() + " status",
                    HttpStatus.BAD_REQUEST, "ORDER_NOT_UPDATABLE");
        }

        orderMapper.updateEntityFromDto(request, order);

        // Update items if provided
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            order.getItems().clear();
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderItemRequest itemRequest : request.getItems()) {
                OrderItem item = orderMapper.toEntity(itemRequest);
                BigDecimal itemTotal = itemRequest.getUnitPrice()
                        .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
                item.setTotalPrice(itemTotal);
                totalAmount = totalAmount.add(itemTotal);
                order.addItem(item);
            }
            order.setTotalAmount(totalAmount);
        }

        Order updatedOrder = orderRepository.save(order);
        log.info("Order updated successfully with ID: {}", updatedOrder.getId());

        return enrichWithUserInfo(orderMapper.toResponse(updatedOrder));
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, Order.OrderStatus status) {
        log.info("Updating status of order ID: {} to: {}", id, status);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        validateStatusTransition(order.getStatus(), status);
        order.setStatus(status);

        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated successfully");

        return enrichWithUserInfo(orderMapper.toResponse(updatedOrder));
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long id) {
        log.info("Cancelling order with ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        // Check if order can be cancelled
        if (order.getStatus() == Order.OrderStatus.SHIPPED ||
            order.getStatus() == Order.OrderStatus.DELIVERED ||
            order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new BusinessException("Cannot cancel order in " + order.getStatus() + " status",
                    HttpStatus.BAD_REQUEST, "ORDER_NOT_CANCELLABLE");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);
        log.info("Order cancelled successfully");

        return enrichWithUserInfo(orderMapper.toResponse(cancelledOrder));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        log.info("Deleting order with ID: {}", id);

        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order", "id", id);
        }

        orderRepository.deleteById(id);
        log.info("Order deleted successfully with ID: {}", id);
    }

    @Override
    public OrderStatistics getOrderStatistics() {
        log.debug("Fetching order statistics");

        return new OrderStatistics(
                orderRepository.count(),
                orderRepository.countByStatus(Order.OrderStatus.PENDING),
                orderRepository.countByStatus(Order.OrderStatus.CONFIRMED),
                orderRepository.countByStatus(Order.OrderStatus.PROCESSING),
                orderRepository.countByStatus(Order.OrderStatus.SHIPPED),
                orderRepository.countByStatus(Order.OrderStatus.DELIVERED),
                orderRepository.countByStatus(Order.OrderStatus.CANCELLED)
        );
    }

    private void validateUserExists(Long userId) {
        try {
            ApiResponse<Boolean> response = userClient.existsById(userId);
            if (response.getData() == null || !response.getData()) {
                throw new BusinessException("User with ID " + userId + " not found",
                        HttpStatus.BAD_REQUEST, "USER_NOT_FOUND");
            }
        } catch (Exception e) {
            log.warn("Could not validate user existence: {}. Proceeding with order creation.", e.getMessage());
            // In case of circuit breaker fallback, we allow order creation
        }
    }

    private OrderResponse enrichWithUserInfo(OrderResponse response) {
        try {
            ApiResponse<UserClient.UserResponse> userResponse = userClient.getUserById(response.getUserId());
            if (userResponse.isSuccess() && userResponse.getData() != null) {
                UserClient.UserResponse user = userResponse.getData();
                response.setUser(OrderResponse.UserInfo.builder()
                        .id(user.id())
                        .fullName(user.fullName())
                        .email(user.email())
                        .build());
            }
        } catch (Exception e) {
            log.warn("Could not fetch user info for user ID: {}. Error: {}", response.getUserId(), e.getMessage());
            // Continue without user info
        }
        return response;
    }

    private String generateOrderNumber() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long counter = orderCounter.incrementAndGet();
        return String.format("ORD-%s-%05d", datePrefix, counter);
    }

    private void validateStatusTransition(Order.OrderStatus from, Order.OrderStatus to) {
        // Define valid status transitions
        boolean valid = switch (from) {
            case PENDING -> to == Order.OrderStatus.CONFIRMED || to == Order.OrderStatus.CANCELLED;
            case CONFIRMED -> to == Order.OrderStatus.PROCESSING || to == Order.OrderStatus.CANCELLED;
            case PROCESSING -> to == Order.OrderStatus.SHIPPED || to == Order.OrderStatus.CANCELLED;
            case SHIPPED -> to == Order.OrderStatus.DELIVERED;
            case DELIVERED -> to == Order.OrderStatus.REFUNDED;
            case CANCELLED, REFUNDED -> false;
        };

        if (!valid) {
            throw new BusinessException(
                    String.format("Invalid status transition from %s to %s", from, to),
                    HttpStatus.BAD_REQUEST, "INVALID_STATUS_TRANSITION");
        }
    }
}
