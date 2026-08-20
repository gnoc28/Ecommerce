package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>{
    Optional<Orders> findById(Long orderId);

    @Query("select o from Orders o " +
            "left join fetch o.items i " +
            "left join fetch i.product " +
            "where o.orderCode = :orderCode")
    Optional<Orders> findByOrderCode(@Param("orderCode") String orderCode);

    @Query("""
        SELECT o FROM Orders o 
        WHERE (:customerCode IS NULL OR o.customerCode = :customerCode)
          AND (:status IS NULL OR o.status = :status)
        ORDER BY o.createdAt DESC
    """)
    Page<Orders> findOrders(
            @Param("customerCode") String customerCode,
            @Param("status") OrderStatus status,
            Pageable pageable);
}
