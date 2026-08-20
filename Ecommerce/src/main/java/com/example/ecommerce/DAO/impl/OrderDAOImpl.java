package com.example.ecommerce.DAO.impl;

import com.example.ecommerce.DAO.OrderDAO;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderDAOImpl implements OrderDAO {

    private final OrdersRepository repository;

    @Override
    public Orders save(Orders order){
        return repository.save(order);
    }

    @Override
    public Page<Orders> findOrders(String customerCode, OrderStatus status, Pageable pageable){
        return repository.findOrders(customerCode, status, pageable);
    }

    @Override
    public Optional<Orders> findByOrderCode(String orderCode){
        return repository.findByOrderCode(orderCode);
    }

    @Override
    public Optional<Orders> findByOrderId(Long orderId){
        return repository.findById(orderId);
    };
}
