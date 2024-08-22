package com.example.ECommerce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDTO {
//    private Integer itemId;
    private Integer orderId;
    private Integer quantity;
    private Integer productId;
}
