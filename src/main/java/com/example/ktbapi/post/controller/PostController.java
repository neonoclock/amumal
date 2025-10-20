package com.example.ktbapi.post.controller;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.post.dto.*;
import com.example.ktbapi.post.service.CommentService;
import com.example.ktbapi.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public ApiResponse<?> list(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int limit,
                               @RequestParam(defaultValue = "date") String sort) {
        List<PostSummaryResponse> data = postService.getAllPosts(page, limit, sort);
        return ApiResponse.ok("posts_fetch_success", data);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> get(@PathVariable Long id) {
        PostDetailResponse data = postService.getPostById(id);
        return ApiResponse.ok("post_fetch_success", data);
    }

    @PostMapping
    public ApiResponse<?> create(@RequestHeader(value = "X-USER-ID", required = false) Long userId,
                                 @RequestHeader(value = "X-USER-NAME", required = false) String authorName,
                                 @Valid @RequestBody PostCreateRequest req) {
        PostDetailResponse data = postService.createPost(userId, authorName, req);
        return ApiResponse.ok("post_create_success", data);
    }

    @PutMapping("/{id}")
    public ApiResponse<?> update(@RequestHeader(value = "X-USER-ID", required = false) Long userId,
                                 @PathVariable Long id,
                                 @Valid @RequestBody PostUpdateRequest req) {
        PostUpdatedResponse data = postService.updatePost(userId, id, req);
        return ApiResponse.ok("post_update_success", data);
    }

    @PostMapping("/{postId}/comments")
    public ApiResponse<?> createComment(@RequestHeader(value = "X-USER-ID", required = false) Long userId,
                                        @RequestHeader(value = "X-USER-NAME", required = false) String authorName,
                                        @PathVariable Long postId,
                                        @Valid @RequestBody CommentCreateOrUpdateRequest req) {
        CommentResponse data = commentService.create(userId, postId, authorName, req);
        return ApiResponse.ok("comment_create_success", data);
    }

    @PatchMapping("/{postId}/comments/{commentId}")
    public ApiResponse<?> updateComment(@RequestHeader(value = "X-USER-ID", required = false) Long userId,
                                        @PathVariable Long postId,
                                        @PathVariable Long commentId,
                                        @Valid @RequestBody CommentCreateOrUpdateRequest req) {
        CommentUpdatedResponse data = commentService.update(userId, postId, commentId, req);
        return ApiResponse.ok("comment_update_success", data);
    }
}
