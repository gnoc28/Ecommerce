package com.example.ecommerce.DAO;

import com.example.ecommerce.entity.Customers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerDAO {
    Optional<Customers> findByCustomerCode(String customerCode);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Customers save(Customers customers);
    Optional<Customers> findByUsername(String username);
    Page<Customers> findAll(Pageable pageable);
    void deleteByCustomerCode(String customerCode);
}
