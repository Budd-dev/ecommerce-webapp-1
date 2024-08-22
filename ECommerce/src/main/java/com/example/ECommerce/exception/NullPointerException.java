package com.example.ECommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NullPointerException extends RuntimeException{
    public NullPointerException(String message) {
        super(message);
    }
}
