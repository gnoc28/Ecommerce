package com.example.ecommerce.DAO;

import com.example.ecommerce.entity.Transactions;

public interface TransactionDAO {
    Transactions save(Transactions transaction);
}
