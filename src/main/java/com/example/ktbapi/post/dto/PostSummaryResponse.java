package com.example.ktbapi.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostSummaryResponse(
        @JsonProperty("post_id") Long postId,
        String title,
        String author,
        @JsonProperty("created_at") String createdAt,
        int views,
        int likes
) {}
