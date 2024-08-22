package com.example.ECommerce.service;

import com.example.ECommerce.dto.request.PaymentRequestDTO;
import com.example.ECommerce.dto.response.PaymentResponseDTO;
import com.example.ECommerce.exception.BadRequestException;
import com.example.ECommerce.exception.ResourceNotFoundException;
import com.example.ECommerce.mapper.PaymentMapper;
import com.example.ECommerce.model.Payment;
import com.example.ECommerce.model.PaymentFactory;
import com.example.ECommerce.model.PaymentProcessor;
import com.example.ECommerce.repository.OrderRepository;
import com.example.ECommerce.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.ECommerce.util.ECommerceUtils.validateId;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private OrderRepository orderRepository;

    public void validatePayment(PaymentRequestDTO paymentRequestDTO) {
        logger.info("Validating payment request: {}", paymentRequestDTO);

        if (paymentRequestDTO.getType() == null || paymentRequestDTO.getType().trim().isEmpty()) {
            logger.error("Payment type is required.");
            throw new BadRequestException("Payment type is required.");
        }

        if (paymentRequestDTO.getOrderId() == null || paymentRequestDTO.getOrderId() <= 0) {
            logger.error("Invalid order ID.");
            throw new BadRequestException("Invalid order ID.");
        }

        if (orderRepository.findById(paymentRequestDTO.getOrderId()).isEmpty()) {
            logger.error("Invalid order ID.");
            throw new BadRequestException("Order with this ID doesn't exist.");
        }

        logger.info("Payment request validated successfully.");
    }

    public List<PaymentResponseDTO> fetchAllPayments() {
        logger.info("Fetching all payments.");
        List<PaymentResponseDTO> payments = paymentRepository.findAll().stream()
                .map(paymentMapper::paymentToPaymentResponseDTO)
                .collect(Collectors.toList());
        logger.info("Fetched {} payments.", payments.size());
        return payments;
    }

    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) throws BadRequestException {
        logger.info("Creating payment with request: {}", paymentRequestDTO);
        validatePayment(paymentRequestDTO);

        Payment payment = paymentMapper.paymentRequestDTOToPayment(paymentRequestDTO);
        PaymentProcessor paymentProcessor = PaymentFactory.getPaymentProcessor(paymentRequestDTO.getType());

        logger.info("Processing payment with processor: {}", paymentProcessor.getClass().getSimpleName());
        paymentProcessor.processPayment(payment);

        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment created successfully with ID: {}", savedPayment.getPaymentId());
        return paymentMapper.paymentToPaymentResponseDTO(savedPayment);
    }

    public PaymentResponseDTO fetchPaymentsById(Integer id) {
        logger.info("Fetching payment by ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Payment not found for ID: {}", id);
                    return new ResourceNotFoundException("Payment dne for id: " + id);
                });
        validateId(id, "Payment");

        logger.info("Payment fetched successfully with ID: {}", id);
        return paymentMapper.paymentToPaymentResponseDTO(payment);
    }

    @Transactional
    public PaymentResponseDTO updatePayment(Integer id, PaymentRequestDTO paymentRequestDTO) {
        logger.info("Updating payment with ID: {}", id);
        Payment payment = paymentRepository
                .findById(id).orElseThrow(() -> {
                    logger.error("Payment not found for ID: {}", id);
                    return new ResourceNotFoundException("Payment dne for id: " + id);
                });
        validateId(id, "Payment");
        validatePayment(paymentRequestDTO);

        paymentMapper.updatePaymentFromPaymentRequestDto(paymentRequestDTO, payment);
        Payment updatedPayment = paymentRepository.save(payment);

        logger.info("Payment updated successfully with ID: {}", id);
        return paymentMapper.paymentToPaymentResponseDTO(updatedPayment);
    }

    @Transactional
    public ResponseEntity<HttpStatus> deletePayment(Integer id) {
        logger.info("Deleting payment with ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Payment not found for ID: {}", id);
                    return new ResourceNotFoundException("Payment dne for id: " + id);
                });

        paymentRepository.delete(payment);
        logger.info("Payment deleted successfully with ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
