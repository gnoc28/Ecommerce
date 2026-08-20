package com.example.ecommerce.DAO;

import com.example.ecommerce.entity.Products;
import com.example.ecommerce.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    Optional<Products> findByProductCode(String productCode);
    Page<Products> findAll(Pageable pageable);
    Products save(Products product);
    Long countByCategoryCode(String categoryCode);
    Page<Products> getProducts(String keyword,
                               String categoryCode,
                               ProductStatus status,
                               Pageable pageable);
    void deleteByProductCode(String productCode);
}
