package com.example.ecommerce.DAO;

import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderDAO {
    Orders save(Orders order);
    Page<Orders> findOrders(String customerCode, OrderStatus status, Pageable pageable);
    Optional<Orders> findByOrderCode(String orderCode);
    Optional<Orders> findByOrderId(Long orderId);
}
