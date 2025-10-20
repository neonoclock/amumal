package com.example.ktbapi.post.mapper;

import com.example.ktbapi.post.dto.CommentResponse;
import com.example.ktbapi.post.dto.PostDetailResponse;
import com.example.ktbapi.post.dto.PostSummaryResponse;
import com.example.ktbapi.post.model.Comment;
import com.example.ktbapi.post.model.Post;

import java.util.List;

public final class PostMapper {
    private PostMapper() {}

    public static PostSummaryResponse toSummary(Post p) {
        if (p == null) return null;
        return new PostSummaryResponse(
                p.getId(), p.getTitle(), p.getAuthor(),
                p.getCreatedAt(), p.getViews(), p.getLikes()
        );
    }

    public static PostDetailResponse toDetail(Post p, List<Comment> comments) {
        if (p == null) return null;
        List<CommentResponse> cmts = comments.stream()
                .map(c -> new CommentResponse(c.getId(), p.getId(), c.getAuthor(), c.getContent(), c.getCreatedAt()))
                .toList();

        return new PostDetailResponse(
                p.getId(), p.getTitle(), p.getContent(), p.getAuthor(),
                p.getImageUrl(), p.getCreatedAt(), p.getViews(), p.getLikes(), cmts
        );
    }
}
