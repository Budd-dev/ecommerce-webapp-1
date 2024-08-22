package com.example.ECommerce.service;

import com.example.ECommerce.dto.request.OrderItemRequestDTO;
import com.example.ECommerce.dto.response.OrderItemResponseDTO;
import com.example.ECommerce.exception.ResourceNotFoundException;
import com.example.ECommerce.mapper.OrderItemMapper;
import com.example.ECommerce.model.OrderItem;
import com.example.ECommerce.model.Product;
import com.example.ECommerce.repository.OrderItemRepository;
import com.example.ECommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemMapper orderItemMapper;

    public List<OrderItemResponseDTO> fetchAllOrderItems() {
        return orderItemRepository.findAll().stream()
                .map(orderItemMapper::orderItemToOrderItemResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderItemResponseDTO createOrderItem(OrderItemRequestDTO orderItemRequestDTO) throws BadRequestException {
        OrderItem orderItem = orderItemMapper.orderItemRequestDTOToOrderItem(orderItemRequestDTO);
        if (orderItem.getQuantity() == null || orderItem.getProductId() == null){
            throw new BadRequestException("Invalid input data: Quantity and product id are required.");
        }
        Product product = productRepository.findById(orderItem.getProductId())
                .orElseThrow(() -> new BadRequestException("Product not found"));
        orderItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));

        OrderItem savedorderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.orderItemToOrderItemResponseDTO(savedorderItem);
    }


    public OrderItemResponseDTO fetchOrderItemsById(Integer id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item dne for id: " + id));
        return orderItemMapper.orderItemToOrderItemResponseDTO(orderItem);
    }

    @Transactional
    public OrderItemResponseDTO updateOrderItem
            (Integer id, OrderItemRequestDTO orderItemRequestDTO) {
        OrderItem orderItem = orderItemRepository
                .findById(id).orElseThrow(()->new ResourceNotFoundException("Order item dne for id: " + id));

        orderItemMapper.updateOrderItemFromOrderItemRequestDto(orderItemRequestDTO, orderItem);
        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.orderItemToOrderItemResponseDTO(updatedOrderItem);
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteOrderItem(Integer id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Order item dne for id: " + id));

        orderItemRepository.delete(orderItem);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
