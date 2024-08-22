package com.example.ECommerce.model;

import java.util.UUID;

public class BankTransferPaymentProcessor implements PaymentProcessor {
    @Override
    public void processPayment(Payment payment) {
        String transactionId = UUID.randomUUID().toString();
        payment.setTransactionId(transactionId);
        payment.setStatus("Completed");
        System.out.println("Processed Bank Transfer payment.");
    }
}
