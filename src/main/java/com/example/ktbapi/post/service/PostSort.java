package com.example.ktbapi.post.service;

import com.example.ktbapi.post.api.PostSortKey;
import com.example.ktbapi.post.model.Post;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class PostSort {
    public static final String DATE  = "date";
    public static final String LIKES = "likes";
    public static final String VIEWS = "views";

    private static final Map<String, Comparator<Post>> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put(DATE,  Comparator.comparing(Post::getCreatedAt).reversed());
        REGISTRY.put(LIKES, Comparator.comparingInt(Post::getLikes).reversed());
        REGISTRY.put(VIEWS, Comparator.comparingInt(Post::getViews).reversed());
    }

    private PostSort() {}

    public static Comparator<Post> resolve(String key) {
        if (key == null) return REGISTRY.get(DATE);
        return REGISTRY.getOrDefault(key.toLowerCase(), REGISTRY.get(DATE));
    }

    public static Comparator<Post> resolve(PostSortKey key) {
        String k = (key == null ? DATE : key.value()); // "date"/"likes"/"views"
        return REGISTRY.getOrDefault(k, REGISTRY.get(DATE));
    }

    public static boolean supports(String key) {
        return key != null && REGISTRY.containsKey(key.toLowerCase());
    }
}

