package com.example.ktbapi.post.service;

import com.example.ktbapi.post.model.Post;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class PostSort {

    private static final Map<String, Comparator<Post>> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put("date", Comparator.comparing(Post::getCreatedAt).reversed());

        REGISTRY.put("likes", Comparator.comparingInt(Post::getLikes).reversed());
        REGISTRY.put("views", Comparator.comparingInt(Post::getViews).reversed());

    }

    private PostSort() {}

    public static Comparator<Post> resolve(String key) {
        if (key == null) return REGISTRY.get("date"); // 기본값
        return REGISTRY.getOrDefault(key.toLowerCase(), REGISTRY.get("date"));
    }

    public static boolean supports(String key) {
        return key != null && REGISTRY.containsKey(key.toLowerCase());
    }
}

