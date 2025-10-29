package com.example.ktbapi.post.api;

public enum PostSortKey {
    DATE, LIKES, VIEWS;

    public String value() {
        return name().toLowerCase();
    }
}
