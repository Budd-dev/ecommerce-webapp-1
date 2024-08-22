package com.example.ECommerce.model;

import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {
    public static PaymentProcessor getPaymentProcessor(String type) {
        return switch (type) {
            case "Credit Card" -> new CreditCardPaymentProcessor();
            case "PayPal" -> new PayPalPaymentProcessor();
            case "Bank Transfer" -> new BankTransferPaymentProcessor();
            default -> throw new IllegalArgumentException("Invalid payment type: " + type);
        };
    }
}
