package com.example.ktbapi.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LikeStatusResponse(
        @JsonProperty("post_id") Long postId,
        @JsonProperty("likes_count") long likesCount,
        @JsonProperty("liked_by_me") boolean likedByMe
) {}
