package com.example.ktbapi.post.service;

import com.example.ktbapi.post.api.PostSortKey;
import com.example.ktbapi.post.dto.*;
import java.util.List;

public interface PostService {
    List<PostSummaryResponse> getAllPosts(int page, int limit, PostSortKey sort);
    PostDetailResponse getPostById(Long id);
    PostDetailResponse createPost(Long userId, String authorName, PostCreateRequest req);
    PostUpdatedResponse updatePost(Long userId, Long postId, PostUpdateRequest req);
}
