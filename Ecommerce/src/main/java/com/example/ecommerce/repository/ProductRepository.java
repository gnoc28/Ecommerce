package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Products;
import com.example.ecommerce.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    Optional<Products> findByProductCode(String productCode);
    Long countByCategoryCode(String categoryCode);
    void deleteByProductCode(String productCode);
    Page<Products> findAll(Pageable pageable);
    @Query("SELECT p FROM Products p WHERE p.status = 'ACTIVE' " +
            "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))"
    )
    Page<Products> searchProductsCustomer(@Param("keyword") String keyword,
                                          Pageable pageable);
    @Query("SELECT p FROM Products p WHERE " +
            ":status IS NULL OR p.status = :status " +
            "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "AND (:categoryCode is null or p.categoryCode like upper(concat('%', :categoryCode, '%')))"
    )
    Page<Products> getProducts(@Param("keyword") String keyword,
                               @Param("categoryCode") String categoryCode,
                               @Param("status") ProductStatus status,
                               Pageable pageable);

}
