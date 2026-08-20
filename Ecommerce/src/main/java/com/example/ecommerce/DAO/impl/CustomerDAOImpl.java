package com.example.ecommerce.DAO.impl;

import com.example.ecommerce.DAO.CustomerDAO;
import com.example.ecommerce.entity.Customers;
import com.example.ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerDAOImpl implements CustomerDAO {

    private final CustomerRepository repository;

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return repository.existsByPhone(phone);
    }

    @Override
    public Customers save(Customers customer) {
        return repository.save(customer);
    }

    @Override
    public Optional<Customers> findByCustomerCode(String customerCode){
        return repository.findByCustomerCode(customerCode);
    }

    @Override
    public Optional<Customers> findByUsername(String username){
        return repository.findByUsername(username);
    }

    @Override
    public Page<Customers> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    @Override
    public void deleteByCustomerCode(String customerCode){
        repository.deleteByCustomerCode(customerCode);
    }

}
