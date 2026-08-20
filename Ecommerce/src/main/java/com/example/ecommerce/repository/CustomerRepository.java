package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Customers;
import com.example.ecommerce.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Customers> findByCustomerCode(String customerCode);
    Optional<Customers> findByUsername(String username);
    void deleteByCustomerCode(String customerCode);
    Page<Customers> findAll(Pageable pageable);
}
