package com.example.ktbapi.common.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("comment_not_found: " + id);
    }
}
