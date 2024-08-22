package com.example.ECommerce.controller;


import com.example.ECommerce.dto.request.OrderRequestDTO;
import com.example.ECommerce.dto.response.OrderResponseDTO;
import com.example.ECommerce.exception.BadRequestException;
import com.example.ECommerce.exception.ResourceNotFoundException;
// import com.example.ECommerce.repository.OrderRepository;
import com.example.ECommerce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequestMapping("/api/ecommerce/orders/")
public class OrderController {
    // @Autowired
    // private OrderRepository orderRepository ;
    @Autowired
    private OrderService orderService;

    // api to fetch all orders
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> fetchAllOrders() {
        log.info("Fetching all orders...");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // api to fetch orders by order id
    @GetMapping("{id}")
    public ResponseEntity<OrderResponseDTO> fetchOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderByOrderId(id));
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<OrderResponseDTO>> fetchAllOrdersByUserId(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getAllOrdersByUserId(id));
    }

    // api to create an order object
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder
      (@RequestBody OrderRequestDTO orderRequestDTO) throws BadRequestException {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO));
    }


    // api to update Order data given its id
    @PutMapping("{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder
    (@PathVariable Integer id, @RequestBody OrderRequestDTO orderRequestDTO) throws ResourceNotFoundException {
        return ResponseEntity.ok(orderService.updateOrder(id, orderRequestDTO));
    }

    // delete order REST api
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteOrderById(@PathVariable Integer id) {
        return orderService.deleteOrder(id);
    }
}
