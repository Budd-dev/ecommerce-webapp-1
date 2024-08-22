package com.example.ECommerce.mapper;

import com.example.ECommerce.dto.request.OrderRequestDTO;
import com.example.ECommerce.dto.response.OrderResponseDTO;
import com.example.ECommerce.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    Order orderRequestDTOToOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO orderToOrderResponseDTO(Order order);
    void updateOrderFromOrderRequestDto(OrderRequestDTO orderRequestDTO, @MappingTarget Order order);
}

