package com.example.ECommerce.controller;


import com.example.ECommerce.dto.request.OrderItemRequestDTO;
import com.example.ECommerce.dto.response.OrderItemResponseDTO;
// import com.example.ECommerce.repository.OrderItemRepository;
import com.example.ECommerce.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ecommerce/order-items/")
public class OrderItemController {
    // @Autowired
    // private OrderItemRepository orderItemRepository ;
    @Autowired
    private OrderItemService orderItemService;

    // api to fetch all order-items
    @GetMapping
    public ResponseEntity<List<OrderItemResponseDTO>> fetchAllOrderItems() {
        log.info("Fetching all order items...");
        return ResponseEntity.ok(orderItemService.fetchAllOrderItems());
    }

    // api to create an order item object
    @PostMapping
    public ResponseEntity<OrderItemResponseDTO> createOrderItem
     (@RequestBody OrderItemRequestDTO orderItemRequestDTO) throws BadRequestException  {
        return ResponseEntity.ok(orderItemService.createOrderItem(orderItemRequestDTO));
    }

    // api to fetch order-items by id
    @GetMapping("{id}")
    public ResponseEntity<OrderItemResponseDTO> fetchOrdersItemById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderItemService.fetchOrderItemsById(id));
    }

    // api to update Order item data given its id
    @PutMapping("{id}")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItem
    (@PathVariable Integer id, @RequestBody OrderItemRequestDTO orderItemRequestDTO) {
        return ResponseEntity.ok(orderItemService.updateOrderItem(id, orderItemRequestDTO));
    }

    // delete order item REST api
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteOrderItemById(@PathVariable Integer id) {
        return orderItemService.deleteOrderItem(id);
    }
}
