package com.example.ECommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Integer paymentId;
    private String type;
    private String status;
    private String transactionId;
    private Integer orderId;
}
