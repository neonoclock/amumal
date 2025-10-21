package com.example.ktbapi.common.exception;

public class AlreadyLikedException extends RuntimeException {

    public AlreadyLikedException() {
        super("already_liked");
    }

    public AlreadyLikedException(Long postId, Long userId) {
        super("User %d already liked post %d".formatted(userId, postId));
    }
}
