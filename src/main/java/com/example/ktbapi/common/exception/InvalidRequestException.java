package com.example.ktbapi.common.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message == null ? "invalid_request" : message);
    }
}
