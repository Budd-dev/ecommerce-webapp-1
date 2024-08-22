package com.example.ECommerce.service;

import com.example.ECommerce.dto.request.OrderItemRequestDTO;
import com.example.ECommerce.dto.request.OrderRequestDTO;
import com.example.ECommerce.dto.response.OrderItemResponseDTO;
import com.example.ECommerce.dto.response.OrderResponseDTO;
import com.example.ECommerce.exception.BadRequestException;
import com.example.ECommerce.exception.ResourceNotFoundException;
import com.example.ECommerce.mapper.OrderItemMapper;
import com.example.ECommerce.mapper.OrderMapper;
import com.example.ECommerce.model.Order;
import com.example.ECommerce.model.OrderItem;
import com.example.ECommerce.model.Product;
import com.example.ECommerce.repository.OrderItemRepository;
import com.example.ECommerce.repository.OrderRepository;
import com.example.ECommerce.repository.ProductRepository;
import com.example.ECommerce.repository.UserRepository;
import com.example.ECommerce.util.ECommerceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.ECommerce.util.ECommerceUtils.validateId;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductRepository productRepository;

    public void validateList(List<Order> orders, Integer id) {
        if (orders.isEmpty()) {
            logger.error("No orders found for user ID: {}", id);
            throw new ResourceNotFoundException("No orders found for user ID: " + id);
        }
    }

    public void validateOrderItemRequestDTO(OrderItemRequestDTO orderItemDTO) {
        logger.info("Validating order item request DTO: {}", orderItemDTO);

        if (orderItemDTO.getQuantity() == null  || orderItemDTO.getQuantity() == 0 || orderItemDTO.getProductId() == null) {
            logger.error("Invalid input data: Valid quantity and product id are required.");
            throw new BadRequestException("Invalid input data: Quantity and product id are required.");
        }

        Product product = productRepository.findById(orderItemDTO.getProductId())
                .orElseThrow(() -> {
                    logger.error("Product not found for ID: {}", orderItemDTO.getProductId());
                    return new BadRequestException("Product not found");
                });

        if (orderItemDTO.getQuantity() > product.getQuantity()) {
            logger.error("Insufficient stock for product ID: {}. Available: {}, Requested: {}",
                    orderItemDTO.getProductId(), product.getQuantity(), orderItemDTO.getQuantity());
            throw new ResourceNotFoundException("Product Id: " + orderItemDTO.getProductId() +
                    " has insufficient stock! \nAvailable stock for selected product is: " + product.getQuantity() +
                    " \nWhereas requested quantity for selected product is: " + orderItemDTO.getQuantity());
        }

        logger.info("Order item request DTO: {} validated successfully.", orderItemDTO);
    }

    public void validateOrderRequestDTO(OrderRequestDTO orderRequestDTO) {
        logger.info("Validating order request DTO: {}", orderRequestDTO);
        validateId(orderRequestDTO.getUserId(), "User");

        if (userRepository.findById(orderRequestDTO.getUserId()).isEmpty()) {
            throw new BadRequestException("User with this ID doesn't exist.");
        }

        if (orderRequestDTO.getOrderItems() == null || orderRequestDTO.getOrderItems().isEmpty()) {
            logger.error("Order items cannot be null or empty");
            throw new BadRequestException("Order items cannot be null or empty");
        }
    }

    public OrderResponseDTO getOrderByOrderId(Integer id) throws BadRequestException, ResourceNotFoundException {
        logger.info("Fetching order by ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found for ID: {}", id);
                    return new ResourceNotFoundException("Order not found for id: " + id);
                });
        validateId(id, "Order");

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
        List<OrderItemResponseDTO> orderItemResponseDTOList = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            OrderItemResponseDTO orderItemResponseDTO = orderItemMapper.orderItemToOrderItemResponseDTO(orderItem);
            orderItemResponseDTOList.add(orderItemResponseDTO);
        }

        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(order);
        orderResponseDTO.setOrderItems(orderItemResponseDTOList);

        logger.info("Fetched order response DTO for ID: {}", id);
        return orderResponseDTO;
    }

    public List<OrderResponseDTO> getAllOrders() {
        logger.info("Fetching all orders.");
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();

        for (Order order : orders) {
            OrderResponseDTO orderResponseDTO = getOrderByOrderId(order.getOrderId());
            orderResponseDTOList.add(orderResponseDTO);
        }

        logger.info("Fetched {} orders.", orderResponseDTOList.size());
        return orderResponseDTOList;
    }

    public OrderItem setOrderItemPrice(OrderItem orderItem) {
        logger.info("Setting price for order item: {}", orderItem);
        Product product = productRepository.findById(orderItem.getProductId())
                .orElseThrow(() -> {
                    logger.error("Product not found for ID: {}", orderItem.getProductId());
                    return new BadRequestException("Product not found");
                });

        orderItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        orderItem.setTotalPrice(orderItem.getTotalPrice().setScale(2, ECommerceUtils.DEFAULT_ROUNDING_MODE));

        product.setQuantity(product.getQuantity() - orderItem.getQuantity());
        productRepository.save(product);
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        logger.info("Price set for order item. Total price: {}", orderItem.getTotalPrice());
        return savedOrderItem;
    }

    public Order setOrderDetails(OrderRequestDTO orderRequestDTO, BigDecimal orderTotalPrice) {
        logger.info("Setting order details with total price: {}", orderTotalPrice);
        Order order = orderMapper.orderRequestDTOToOrder(orderRequestDTO);
        LocalDate today = LocalDate.now();
        String date = today.toString();

        order.setDate(date);
        order.setTotalPrice(orderTotalPrice);
        order.setStatus("Completed");

        Order savedOrder = orderRepository.save(order);
        logger.info("Order details set and saved. Order ID: {}", savedOrder.getOrderId());
        return savedOrder;
    }

    public void setOrderIdFromOrderRequestDTO(OrderItemResponseDTO itemResponseDTO, Order order) {
        logger.info("Setting order ID {} in order item response DTO.", order.getOrderId());
        itemResponseDTO.setOrderId(order.getOrderId());
        OrderItem orderItem = orderItemMapper.orderItemResponseDTOToOrderItem(itemResponseDTO);

        orderItemRepository.save(orderItem);
        logger.info("Order ID set in order item response DTO and saved.");
    }

    public List<OrderResponseDTO> getAllOrdersByUserId(int id) throws BadRequestException, ResourceNotFoundException {
        logger.info("Fetching all orders by user ID: {}", id);
        validateId(id, "User");
        List<Order> orders = orderRepository.findByUserId(id);
        validateList(orders, id);

        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        for (Order order : orders) {
            OrderResponseDTO orderResponseDTO = getOrderByOrderId(order.getOrderId());
            orderResponseDTOList.add(orderResponseDTO);
        }

        logger.info("Fetched {} orders for user ID: {}", orderResponseDTOList.size(), id);
        return orderResponseDTOList;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) throws BadRequestException {
        logger.info("Creating order with request DTO: {}", orderRequestDTO);
        validateOrderRequestDTO(orderRequestDTO);

        List<OrderItemRequestDTO> itemRequestDTOlist = orderRequestDTO.getOrderItems();
        List<OrderItemResponseDTO> itemResponseDTOList = new ArrayList<>();
        OrderItem orderItem;
        BigDecimal orderTotalPrice = ECommerceUtils.initializeBigDecimal();

        for (OrderItemRequestDTO iRequestDTO : itemRequestDTOlist) {
            validateOrderItemRequestDTO(iRequestDTO);
            logger.info("Order request DTO validated successfully.");
            orderItem = orderItemMapper.orderItemRequestDTOToOrderItem(iRequestDTO);

            orderItem = setOrderItemPrice(orderItem);
            orderTotalPrice = orderTotalPrice.add(orderItem.getTotalPrice());

            OrderItemResponseDTO iResponseDTO = orderItemMapper.orderItemToOrderItemResponseDTO(orderItem);
            itemResponseDTOList.add(iResponseDTO);
        }

        Order order = setOrderDetails(orderRequestDTO, orderTotalPrice);
        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(order);
        orderResponseDTO.setOrderItems(itemResponseDTOList);

        for (OrderItemResponseDTO itemResponseDTO : orderResponseDTO.getOrderItems()) {
            setOrderIdFromOrderRequestDTO(itemResponseDTO, order);
        }

        logger.info("Order created successfully. Order ID: {}", order.getOrderId());
        return orderResponseDTO;
    }

    @Transactional
    public OrderResponseDTO updateOrder(Integer id, OrderRequestDTO orderRequestDTO) throws ResourceNotFoundException {
        logger.info("Updating order with ID: {} using request DTO: {}", id, orderRequestDTO);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found for ID: {}", id);
                    return new ResourceNotFoundException("Order dne for id: " + id);
                });
        validateId(id, "Order");
        validateOrderRequestDTO(orderRequestDTO);

        orderMapper.updateOrderFromOrderRequestDto(orderRequestDTO, order);
        Order updatedOrder = orderRepository.save(order);
        logger.info("Order updated successfully. Order ID: {}", updatedOrder.getOrderId());
        return getOrderByOrderId(updatedOrder.getOrderId());
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteOrder(Integer id) {
        logger.info("Deleting order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found for ID: {}", id);
                    return new ResourceNotFoundException("Order dne for id: " + id);
                });
        validateId(id, "Order");

        orderRepository.delete(order);
        logger.info("Order deleted successfully. Order ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
