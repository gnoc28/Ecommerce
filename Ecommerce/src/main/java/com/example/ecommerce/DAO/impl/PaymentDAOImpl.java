package com.example.ecommerce.DAO.impl;

import com.example.ecommerce.DAO.PaymentDAO;
import com.example.ecommerce.entity.Payments;
import com.example.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentDAOImpl implements PaymentDAO {

    private final PaymentRepository repository;

    public Optional<Payments> findByOrderCode(String orderCode){
        return repository.findByOrderCode(orderCode);
    }

    public Payments save(Payments payment){
        return repository.save(payment);
    }
}
