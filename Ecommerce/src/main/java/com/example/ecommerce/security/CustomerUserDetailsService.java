package com.example.ecommerce.security;

import com.example.ecommerce.DAO.CustomerDAO;
import com.example.ecommerce.entity.Customers;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerDAO customerDAO;

    @Override
    public @Nonnull UserDetails loadUserByUsername(@Nonnull String username) throws UsernameNotFoundException {
        Customers customer = customerDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));
        return new CustomerUserDetails(customer);
    }
}
