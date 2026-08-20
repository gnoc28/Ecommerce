package com.example.ecommerce.service.impl;

import com.example.ecommerce.DAO.CartDAO;
import com.example.ecommerce.DAO.CustomerDAO;
import com.example.ecommerce.DTO.request.ChangePasswordRequest;
import com.example.ecommerce.DTO.request.LoginRequest;
import com.example.ecommerce.DTO.request.RegisterRequest;
import com.example.ecommerce.DTO.request.UpdateCustomerRequest;
import com.example.ecommerce.DTO.response.CustomerDetailResponse;
import com.example.ecommerce.DTO.response.LoginResponse;
import com.example.ecommerce.entity.Carts;
import com.example.ecommerce.entity.Customers;
import com.example.ecommerce.enums.CustomerStatus;
import com.example.ecommerce.enums.RoleType;
import com.example.ecommerce.exception.AppException;
import com.example.ecommerce.mapper.CustomerMapper;
import com.example.ecommerce.security.CustomerUserDetails;
import com.example.ecommerce.security.JwtProvider;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.utils.PageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ecommerce.exception.AppException.*;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;
    private final CartDAO cartDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomerMapper mapper;

    @Override
    @Transactional
    public void createdCustomer(RegisterRequest request) {
        if (customerDAO.existsByUsername(request.getUsername())) {
            throw new DataExistsException("Tên đăng nhập đã tồn tại");
        }
        if (customerDAO.existsByEmail(request.getEmail())) {
            throw new DataExistsException("Email đã tồn tại");
        }
        if (customerDAO.existsByPhone(request.getPhone())) {
            throw new DataExistsException("Số điện thoại đã tồn tại");
        }

        String hashPassword = passwordEncoder.encode(request.getPassword());

        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String custCode = "CUST" + datePart + randomPart;

        Customers customer = Customers.builder()
                .customerCode(custCode)
                .status(CustomerStatus.ACTIVE)
                .username(request.getUsername())
                .name(request.getName())
                .email(request.getEmail())
                .password(hashPassword)
                .phone(request.getPhone())
                .role(RoleType.CUSTOMER)
                .build();

        customerDAO.save(customer);

        Carts cart = new Carts();
        cart.setCartCode("CART" + randomPart + datePart);
        cart.setCustomer(customer);
        cartDAO.save(cart);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Customers customers = customerDAO.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Username hoặc mật khẩu không đúng."));

        if (!passwordEncoder.matches(request.getPassword(), customers.getPassword())) {
            throw new UnauthorizedException("Username hoặc mật khẩu không đúng.");
        }

        if (customers.getStatus() != CustomerStatus.ACTIVE) {
            throw new UnauthorizedException("Tài khoản của bạn bị khóa");
        }

        String token = jwtProvider.generateToken(customers.getUsername());
        return new LoginResponse(token);
    }

    @Override
    public Object getCustomers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Customers> customersPage = customerDAO.findAll(pageable);

        Page<CustomerDetailResponse> responsePage = customersPage.map(mapper::toCustomerDetailResponse);
        return PageUtils.toPageResponse(responsePage);
    }

    @Override
    @Transactional
    public CustomerDetailResponse getCustomerDetail(String customerCode) {
        Customers customer;

        if (customerCode == null || customerCode.isBlank()) {
            String currentUsername = CustomerUserDetails.getCurrentUsername();
            customer = customerDAO.findByUsername(currentUsername)
                    .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy profile"));
        } else {
            if (!CustomerUserDetails.isAdmin()) {
                throw new AppException.ForbiddenException("Bạn không có quyền xem thông tin người dùng này");
            }

            customer = customerDAO.findByCustomerCode(customerCode)
                    .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy khách hàng với mã: " + customerCode));
        }

        return mapper.toCustomerDetailResponse(customer);
    }

    @Override
    @Transactional
    public void updateCustomer(String customerCode, UpdateCustomerRequest request) {
        Customers customer = customerDAO.findByCustomerCode(customerCode)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy customer"));

        if (request.getEmail() != null && !customer.getEmail().equals(request.getEmail()) && customerDAO.existsByEmail(request.getEmail())) {
            throw new DataExistsException("Đã tồn tại email");
        }

        if (request.getPhone() != null && !customer.getPhone().equals(request.getPhone()) && customerDAO.existsByPhone(request.getPhone())) {
            throw new DataExistsException("Đã tồn tại sdt");
        }

        mapper.updateCustomerFromDto(request, customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(String customerCode) {
        customerDAO.findByCustomerCode(customerCode)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy customer"));
        customerDAO.deleteByCustomerCode(customerCode);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {

        String currentUsername = CustomerUserDetails.getCurrentUsername();

        Customers customer = customerDAO.findByUsername(currentUsername)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy customer"));
        String oldPass = request.getOldPassword();
        String newPass = request.getNewPassword();
        String accessPass = request.getAccessPassword();
        if (!newPass.equals(accessPass)) {
            throw new DataExistsException("Mật khẩu không khớp");
        }
        if (!passwordEncoder.matches(oldPass, customer.getPassword())) {
            throw new DataExistsException("Mật khẩu ko chính xác");
        }
        if (passwordEncoder.matches(request.getNewPassword(), customer.getPassword())) {
            throw new DataExistsException("Mật khẩu không được trùng với mật khẩu cũ");
        }
        customer.setPassword(passwordEncoder.encode(newPass));
        customerDAO.save(customer);
    }
}
