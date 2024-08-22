package com.example.ECommerce.model;

import java.util.UUID;

public class CreditCardPaymentProcessor implements PaymentProcessor {
    @Override
    public void processPayment(Payment payment) {
        String transactionId = UUID.randomUUID().toString();
        payment.setTransactionId(transactionId);
        payment.setStatus("Completed");
        System.out.println("Processed Credit Card payment.");
    }
}
