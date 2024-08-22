package com.example.ECommerce.mapper;

import com.example.ECommerce.dto.request.OrderItemRequestDTO;
import com.example.ECommerce.dto.response.OrderItemResponseDTO;
import com.example.ECommerce.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
//import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
//    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);
    OrderItem orderItemRequestDTOToOrderItem(OrderItemRequestDTO orderItemRequestDTO);
    OrderItem orderItemResponseDTOToOrderItem(OrderItemResponseDTO orderItemResponseDTO);
    OrderItemResponseDTO orderItemToOrderItemResponseDTO(OrderItem orderItem);
    void updateOrderItemFromOrderItemRequestDto(OrderItemRequestDTO orderItemRequestDTO, @MappingTarget OrderItem orderItem);

}
