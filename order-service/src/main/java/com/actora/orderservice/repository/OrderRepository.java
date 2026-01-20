package com.actora.orderservice.repository;

import com.actora.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order entity with custom query methods.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find order by order number.
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Check if an order with the given order number exists.
     */
    boolean existsByOrderNumber(String orderNumber);

    /**
     * Find all orders by user ID.
     */
    List<Order> findByUserId(Long userId);

    /**
     * Find all orders by user ID with pagination.
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * Find all orders by status.
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * Find all orders by status with pagination.
     */
    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);

    /**
     * Find orders by user ID and status.
     */
    Page<Order> findByUserIdAndStatus(Long userId, Order.OrderStatus status, Pageable pageable);

    /**
     * Search orders by order number or description.
     */
    @Query("SELECT o FROM Order o WHERE " +
           "(:searchTerm IS NULL OR LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(o.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND (:status IS NULL OR o.status = :status) " +
           "AND (:userId IS NULL OR o.userId = :userId)")
    Page<Order> searchOrders(@Param("searchTerm") String searchTerm,
                            @Param("status") Order.OrderStatus status,
                            @Param("userId") Long userId,
                            Pageable pageable);

    /**
     * Find orders created within a date range.
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Count orders by status.
     */
    long countByStatus(Order.OrderStatus status);

    /**
     * Count orders by user ID.
     */
    long countByUserId(Long userId);
}
