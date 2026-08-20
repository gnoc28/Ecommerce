package com.example.ecommerce.mapper;

import com.example.ecommerce.DTO.request.UpdateCustomerRequest;
import com.example.ecommerce.DTO.response.CustomerDetailResponse;
import com.example.ecommerce.entity.Customers;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    CustomerDetailResponse toCustomerDetailResponse(Customers customer);

    void updateCustomerFromDto(UpdateCustomerRequest dto, @MappingTarget Customers customers);
}

