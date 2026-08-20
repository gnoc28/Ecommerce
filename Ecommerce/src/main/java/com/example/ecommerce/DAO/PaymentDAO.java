package com.example.ecommerce.DAO;

import com.example.ecommerce.entity.Payments;

import java.util.Optional;

public interface PaymentDAO {
    Optional<Payments> findByOrderCode(String orderCode);
    Payments save(Payments payment);
}

