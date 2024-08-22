package com.example.ECommerce.controller;


import com.example.ECommerce.dto.request.PaymentRequestDTO;
import com.example.ECommerce.dto.response.PaymentResponseDTO;
import com.example.ECommerce.exception.BadRequestException;
// import com.example.ECommerce.repository.PaymentRepository;
import com.example.ECommerce.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/ecommerce/payments/")
public class PaymentController {
    // @Autowired
    // private PaymentRepository paymentRepository ;
    @Autowired
    private PaymentService paymentService;

    // api to fetch all payments
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> fetchAllPayments() {
        return ResponseEntity.ok(paymentService.fetchAllPayments());
    }

    // api to create a payment object
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) throws BadRequestException {
        return ResponseEntity.ok(paymentService.createPayment(paymentRequestDTO));
    }

    // api to fetch payments by id
    @GetMapping("{id}")
    public ResponseEntity<PaymentResponseDTO> fetchPaymentsById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.fetchPaymentsById(id));
    }

    // api to update payment data given its id
    @PutMapping("{id}")
    public ResponseEntity<PaymentResponseDTO> updatePayment
    (@PathVariable Integer id, @RequestBody PaymentRequestDTO paymentRequestDTO) {
        return ResponseEntity.ok(paymentService.updatePayment(id, paymentRequestDTO));
    }
//
    // delete payment REST api
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deletePaymentById(@PathVariable Integer id) {
        return paymentService.deletePayment(id);
    }
}
