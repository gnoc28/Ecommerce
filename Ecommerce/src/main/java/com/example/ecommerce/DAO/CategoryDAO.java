package com.example.ecommerce.DAO;

import com.example.ecommerce.entity.Categories;

import java.util.List;
import java.util.Optional;

public interface CategoryDAO {
    Optional<Categories> findByCategoryCode(String categoryCode);
    List<Categories> findAll();
    boolean existsByCategoryCode(String categoryCode);
    boolean existsByName(String name);
    Categories save(Categories categories);

}
