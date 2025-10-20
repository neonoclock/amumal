package com.example.ktbapi.common.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("post_not_found: " + id);
    }
}
