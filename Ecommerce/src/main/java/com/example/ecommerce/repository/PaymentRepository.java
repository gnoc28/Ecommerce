package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, Long> {
    Optional<Payments> findByOrderCode(String orderCode);
}
