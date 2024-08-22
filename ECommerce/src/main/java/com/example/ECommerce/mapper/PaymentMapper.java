package com.example.ECommerce.mapper;

import com.example.ECommerce.dto.request.PaymentRequestDTO;
import com.example.ECommerce.dto.response.PaymentResponseDTO;
import com.example.ECommerce.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);
    Payment paymentRequestDTOToPayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO paymentToPaymentResponseDTO(Payment payment);
    void updatePaymentFromPaymentRequestDto(PaymentRequestDTO paymentRequestDTO, @MappingTarget Payment payment);
}
