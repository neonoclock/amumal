package com.example.ktbapi.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentUpdatedResponse(
        @JsonProperty("comment_id") Long commentId,
        @JsonProperty("updated_at") String updatedAt
) {}
