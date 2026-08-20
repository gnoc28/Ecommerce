package com.example.ecommerce.DAO.impl;

import com.example.ecommerce.DAO.TransactionDAO;
import com.example.ecommerce.entity.Transactions;
import com.example.ecommerce.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionDAOImpl implements TransactionDAO {

    private final TransactionRepository repository;

    @Override
    public Transactions save(Transactions transaction){
        return repository.save(transaction);
    }

}
