package com.example.ktbapi.common.exception;

public class NotLikedException extends RuntimeException {

    public NotLikedException() {
        super("not_liked");
    }

    public NotLikedException(Long postId, Long userId) {
        super("User %d hasn't liked post %d".formatted(userId, postId));
    }
}
