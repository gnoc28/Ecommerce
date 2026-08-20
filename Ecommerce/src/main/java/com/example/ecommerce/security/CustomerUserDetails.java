package com.example.ecommerce.security;

import com.example.ecommerce.entity.Customers;
import com.example.ecommerce.enums.CustomerStatus;
import com.example.ecommerce.exception.AppException;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomerUserDetails implements UserDetails {

    private final Customers customers;

    public CustomerUserDetails(Customers customers){
        this.customers = customers;
    }

    @Override
    public @Nonnull Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_" + customers.getRole().name()));
    }
    @Override
    public String getPassword() {
        return customers.getPassword();
    }

    @Override
    public @Nonnull String getUsername() {
        return customers.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return customers.getStatus() == CustomerStatus.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getCustomerCode() {
        return customers.getCustomerCode();
    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AppException.UnauthorizedException("Bạn chưa đăng nhập!");
        }

        return authentication.getName();
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    public static String getCurrentCustomerCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AppException.UnauthorizedException("Bạn chưa đăng nhập!");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomerUserDetails userDetails) {
            return userDetails.getCustomerCode();
        }

        throw new AppException.UnauthorizedException("Không thể lấy mã khách hàng!");
    }

}
