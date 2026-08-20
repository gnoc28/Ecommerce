package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Inventories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventories, Long> {
    Optional<Inventories> findByInventoryCode(String inventoryCode);
    Optional<Inventories> findByProduct_ProductCode(String productCode);
    Page<Inventories> findAll(Pageable pageable);
}
