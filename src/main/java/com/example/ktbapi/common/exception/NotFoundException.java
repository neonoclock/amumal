package com.example.ktbapi.common.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("not_found");
    }
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
