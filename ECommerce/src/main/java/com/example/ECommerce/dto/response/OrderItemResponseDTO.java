package com.example.ECommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Integer itemId;

    private Integer quantity;
    private BigDecimal totalPrice;
    private Integer orderId;
    private Integer productId;
}
