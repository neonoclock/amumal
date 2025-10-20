package com.example.ktbapi.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LikeCountResponse(
        @JsonProperty("post_id") Long postId,
        @JsonProperty("likes_count") long likesCount
) {}
