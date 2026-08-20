package com.example.ecommerce.DAO.impl;

import com.example.ecommerce.DAO.CategoryDAO;
import com.example.ecommerce.entity.Categories;
import com.example.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryDAOImpl implements CategoryDAO {

    private final CategoryRepository repository;

    @Override
    public Optional<Categories> findByCategoryCode(String categoryCode){
        return repository.findByCategoryCode(categoryCode);
    }

    @Override
    public List<Categories> findAll(){
        return repository.findAll();
    }

    @Override
    public boolean existsByCategoryCode(String categoryCode){
        return repository.existsByCategoryCode(categoryCode);
    }

    @Override
    public boolean existsByName(String name){
        return repository.existsByName(name);
    }

    @Override
    public Categories save(Categories categories){
        return repository.save(categories);
    }

}
