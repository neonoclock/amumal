package com.example.ktbapi.post.controller;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.Msg;
import com.example.ktbapi.common.dto.IdResponse;
import com.example.ktbapi.post.dto.CommentCreateOrUpdateRequest;
import com.example.ktbapi.post.dto.CommentResponse;
import com.example.ktbapi.post.dto.CommentUpdatedResponse;
import com.example.ktbapi.post.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ApiResponse<?> create(@PathVariable Long postId,
                                 @RequestHeader(value = "X-USER-ID", required = false) Long userId,
                                 @RequestHeader(value = "X-USER-NAME", required = false) String authorName,
                                 @Valid @RequestBody CommentCreateOrUpdateRequest req) {
        CommentResponse data = commentService.create(userId, postId, authorName, req);
        return ApiResponse.ok(Msg.Success.COMMENT_CREATE, data);
    }

    @PatchMapping("/{commentId}")
    public ApiResponse<?> update(@PathVariable Long postId,
                                 @PathVariable Long commentId,
                                 @RequestHeader(value = "X-USER-ID", required = false) Long userId,
                                 @Valid @RequestBody CommentCreateOrUpdateRequest req) {
        CommentUpdatedResponse data = commentService.update(userId, postId, commentId, req);
        return ApiResponse.ok(Msg.Success.COMMENT_UPDATE, data);
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<?> delete(@PathVariable Long postId,
                                 @PathVariable Long commentId,
                                 @RequestHeader(value = "X-USER-ID", required = false) Long userId) {
        commentService.delete(userId, postId, commentId);
        return ApiResponse.ok(Msg.Success.COMMENT_DELETE, new IdResponse(commentId));
    }
}
