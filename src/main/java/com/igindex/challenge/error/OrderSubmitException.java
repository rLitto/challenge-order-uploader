package com.igindex.challenge.error;

public class OrderSubmitException extends RuntimeException {
    public OrderSubmitException(String message) {
        super(message);
    }
}
