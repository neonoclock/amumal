package com.example.ktbapi.post.controller;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.post.dto.CommentCreateOrUpdateRequest;
import com.example.ktbapi.post.dto.PostCreateRequest;
import com.example.ktbapi.post.dto.PostUpdateRequest;
import com.example.ktbapi.post.service.PostService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {
  private final PostService service;

  public PostController(PostService service) {
    this.service = service;
  }

  // 게시글 목록 조회
  @GetMapping
  public ResponseEntity<ApiResponse<?>> getPosts(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(defaultValue = "date") String sort
  ) {
    var data = service.getAllPosts(page, limit, sort);
    return ResponseEntity.ok(ApiResponse.ok("posts_fetch_success", data));
  }

  // 게시글 상세 조회
  @GetMapping("/{postId}")
  public ResponseEntity<ApiResponse<?>> getPostById(@PathVariable Long postId) {
    var data = service.getPostById(postId);
    return ResponseEntity.ok(ApiResponse.ok("post_fetch_success", data));
  }

  // 게시글 생성
  @PostMapping
  public ResponseEntity<ApiResponse<?>> createPost(
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader(value = "X-User-Name", defaultValue = "익명") String userName,
      @RequestBody PostCreateRequest req
  ) {
    var data = service.createPost(userId, userName, req);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.ok("post_create_success", data));
  }

  // 게시글 수정 
  @PatchMapping("/{postId}")
  public ResponseEntity<ApiResponse<?>> updatePost(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long postId,
      @RequestBody PostUpdateRequest req
  ) {
    var data = service.updatePost(userId, postId, req);
    return ResponseEntity.ok(ApiResponse.ok("post_update_success", data));
  }

  // 댓글 생성
  @PostMapping("/{postId}/comments")
  public ResponseEntity<ApiResponse<?>> createComment(
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader(value = "X-User-Name", defaultValue = "익명") String userName,
      @PathVariable Long postId,
      @RequestBody CommentCreateOrUpdateRequest req
  ) {
    var data = service.createComment(userId, postId, userName, req);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.ok("comment_create_success", data));
  }

  // 댓글 수정
  @PatchMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<ApiResponse<?>> updateComment(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long postId,
      @PathVariable Long commentId,
      @RequestBody CommentCreateOrUpdateRequest req
  ) {
    var data = service.updateComment(userId, postId, commentId, req);
    return ResponseEntity.ok(ApiResponse.ok("comment_update_success", data));
  }

  // 댓글 삭제
  @DeleteMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<ApiResponse<Void>> deleteComment(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long postId,
      @PathVariable Long commentId
  ) {
    service.deleteComment(userId, postId, commentId);
    return ResponseEntity.ok(ApiResponse.ok("comment_delete_success", null));
  }

  // 좋아요 추가
  @PostMapping("/{postId}/likes")
  public ResponseEntity<ApiResponse<?>> addLike(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long postId
  ) {
    var data = service.addLike(userId, postId);
    return ResponseEntity.ok(ApiResponse.ok("like_add_success", data));
  }

  // 좋아요 취소
  @DeleteMapping("/{postId}/likes")
  public ResponseEntity<ApiResponse<?>> removeLike(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long postId
  ) {
    var data = service.removeLike(userId, postId);
    return ResponseEntity.ok(ApiResponse.ok("like_remove_success", data));
  }

  // 좋아요 조회
  @GetMapping("/{postId}/likes")
  public ResponseEntity<ApiResponse<?>> getLikes(
      @PathVariable Long postId,
      @RequestHeader(value = "X-User-Id", required = false) Long userId
  ) {
    var data = service.getLikes(postId, userId);
    return ResponseEntity.ok(ApiResponse.ok("like_fetch_success", data));
  }
}
