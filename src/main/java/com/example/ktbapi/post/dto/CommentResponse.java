package com.example.ktbapi.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentResponse(
        @JsonProperty("comment_id") Long commentId,
        @JsonProperty("post_id") Long postId,
        String author,
        String content,
        @JsonProperty("created_at") String createdAt
) {}
