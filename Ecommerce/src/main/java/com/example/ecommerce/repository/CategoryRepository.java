package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Long> {
    Optional<Categories> findByCategoryCode(String categoryCode);
    boolean existsByCategoryCode(String categoryCode);
    boolean existsByName(String name);
}
