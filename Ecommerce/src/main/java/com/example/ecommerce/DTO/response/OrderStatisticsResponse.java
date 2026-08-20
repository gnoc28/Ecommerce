package com.example.ecommerce.DTO.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatisticsResponse {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Map<String, Long> statusCounts;
}
