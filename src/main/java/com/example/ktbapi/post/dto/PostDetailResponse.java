package com.example.ktbapi.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PostDetailResponse(
        @JsonProperty("post_id") Long postId,
        String title,
        String content,
        String author,
        @JsonProperty("image_url") String imageUrl,
        @JsonProperty("created_at") String createdAt,
        int views,
        int likes,
        List<CommentResponse> comments
) {}
