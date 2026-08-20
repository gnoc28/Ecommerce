package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Carts, Long> {
    Optional<Carts> findByCustomer_Username(String username);
}
