package com.example.ECommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Integer orderId;
    private Integer userId;
    private BigDecimal totalPrice;
    private String date;
    private String status;

    private List<OrderItemResponseDTO> orderItems = new ArrayList<>();
}
