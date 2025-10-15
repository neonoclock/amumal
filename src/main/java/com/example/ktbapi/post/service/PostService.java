package com.example.ktbapi.post.service;

import com.example.ktbapi.post.dto.CommentCreateOrUpdateRequest;
import com.example.ktbapi.post.dto.PostCreateRequest;
import com.example.ktbapi.post.dto.PostUpdateRequest;
import com.example.ktbapi.post.model.Comment;
import com.example.ktbapi.post.model.Post;
import com.example.ktbapi.post.repo.CommentRepository;
import com.example.ktbapi.post.repo.LikeRepository;
import com.example.ktbapi.post.repo.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class PostService {
  private final PostRepository postRepo;
  private final CommentRepository commentRepo;
  private final LikeRepository likeRepo;

  public PostService(PostRepository postRepo, CommentRepository commentRepo, LikeRepository likeRepo) {
    this.postRepo = postRepo;
    this.commentRepo = commentRepo;
    this.likeRepo = likeRepo;
  }

  // 게시글 목록 
  public List<Map<String, Object>> getAllPosts(int page, int limit, String sort) {
    List<Post> posts = postRepo.findAll();
    if ("date".equalsIgnoreCase(sort)) Collections.reverse(posts);

    int start = Math.max(0, (page - 1) * limit);
    int end = Math.min(start + limit, posts.size());
    List<Post> paged = posts.subList(start, end);

    List<Map<String, Object>> result = new ArrayList<>();
    for (Post p : paged) {
      Map<String, Object> m = new LinkedHashMap<>();
      m.put("post_id", p.getId());
      m.put("title", p.getTitle());
      m.put("author", p.getAuthor());
      m.put("created_at", p.getCreatedAt());
      m.put("views", p.getViews());
      m.put("likes", p.getLikes());
      result.add(m);
    }
    return result;
  }

  // 게시글 상세 조회
  public Map<String, Object> getPostById(Long id) {
    Post post = postRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    Post updated = new Post(post.getId(), post.getTitle(), post.getContent(), post.getAuthor(),
        post.getImageUrl(), post.getCreatedAt(), post.getViews() + 1, post.getLikes());
    postRepo.save(updated);

    Map<String, Object> result = new LinkedHashMap<>();
    result.put("post_id", updated.getId());
    result.put("title", updated.getTitle());
    result.put("content", updated.getContent());
    result.put("author", updated.getAuthor());
    result.put("image_url", updated.getImageUrl());
    result.put("created_at", updated.getCreatedAt());
    result.put("views", updated.getViews());
    result.put("likes", updated.getLikes());

    List<Map<String, Object>> comments = new ArrayList<>();
    for (Comment c : commentRepo.findAllByPostId(id)) {
      Map<String, Object> cm = new LinkedHashMap<>();
      cm.put("comment_id", c.getId());
      cm.put("author", c.getAuthor());
      cm.put("content", c.getContent());
      cm.put("created_at", c.getCreatedAt());
      comments.add(cm);
    }
    result.put("comments", comments);

    return result;
  }

  // 게시글 생성
  public Map<String, Object> createPost(Long userId, String authorName, PostCreateRequest req) {
    if (req == null || req.title == null || req.content == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    String now = java.time.LocalDateTime.now().withNano(0).toString().replace('T', ' ');
    Post newPost = new Post(null, req.title, req.content, authorName, req.image_url, now, 0, 0);
    Post saved = postRepo.save(newPost);

    Map<String, Object> result = new LinkedHashMap<>();
    result.put("post_id", saved.getId());
    result.put("title", saved.getTitle());
    result.put("content", saved.getContent());
    result.put("author", saved.getAuthor());
    result.put("image_url", saved.getImageUrl());
    result.put("created_at", saved.getCreatedAt());
    result.put("views", saved.getViews());
    result.put("likes", saved.getLikes());
    return result;
  }

  // 게시글 수정
  public Map<String, Object> updatePost(Long userId, Long postId, PostUpdateRequest req) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
    if (req == null || req.title == null || req.content == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");

    Post post = postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    Post patched = new Post(
        post.getId(),
        req.title,
        req.content,
        post.getAuthor(),
        (req.image_url != null ? req.image_url : post.getImageUrl()),
        post.getCreatedAt(),
        post.getViews(),
        post.getLikes()
    );
    postRepo.save(patched);

    Map<String, Object> result = new LinkedHashMap<>();
    result.put("post_id", patched.getId());
    result.put("title", patched.getTitle());
    result.put("content", patched.getContent());
    result.put("image_url", patched.getImageUrl());
    result.put("updated_at", java.time.LocalDateTime.now().withNano(0).toString().replace('T',' '));
    return result;
  }

  // 댓글 생성
  public Map<String, Object> createComment(Long userId, Long postId, String authorName, CommentCreateOrUpdateRequest req) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
    if (req == null || req.content == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");

    postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    String now = java.time.LocalDateTime.now().withNano(0).toString().replace('T', ' ');
    Comment saved = commentRepo.addComment(postId, new Comment(null, authorName, req.content, now));

    Map<String, Object> result = new LinkedHashMap<>();
    result.put("comment_id", saved.getId());
    result.put("post_id", postId);
    result.put("author", saved.getAuthor());
    result.put("content", saved.getContent());
    result.put("created_at", saved.getCreatedAt());
    return result;
  }

  // ===== 댓글 수정 =====
  public Map<String, Object> updateComment(Long userId, Long postId, Long commentId, CommentCreateOrUpdateRequest req) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
    if (req == null || req.content == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");

    postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));
    Comment c = commentRepo.findComment(postId, commentId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment_not_found"));

    c.setContent(req.content);

    Map<String, Object> result = new LinkedHashMap<>();
    result.put("comment_id", c.getId());
    result.put("updated_at", java.time.LocalDateTime.now().withNano(0).toString().replace('T',' '));
    return result;
  }

  // 댓글 삭제
  public void deleteComment(Long userId, Long postId, Long commentId) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    boolean ok = commentRepo.deleteComment(postId, commentId);
    if (!ok) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "comment_not_found");
  }

  // 좋아요 추가
  public Map<String, Object> addLike(Long userId, Long postId) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    Post post = postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    boolean added = likeRepo.addLike(userId, postId);
    if (!added)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "already_liked");

    Post updated = new Post(post.getId(), post.getTitle(), post.getContent(), post.getAuthor(),
        post.getImageUrl(), post.getCreatedAt(), post.getViews(), post.getLikes() + 1);
    postRepo.save(updated);

    Map<String, Object> result = new LinkedHashMap<>();
    result.put("post_id", postId);
    result.put("likes_count", likeRepo.countLikes(postId));
    return result;
  }

  // 좋아요 취소
  public Map<String, Object> removeLike(Long userId, Long postId) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    Post post = postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    boolean removed = likeRepo.removeLike(userId, postId);
    if (!removed)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not_liked");

    Post updated = new Post(post.getId(), post.getTitle(), post.getContent(), post.getAuthor(),
        post.getImageUrl(), post.getCreatedAt(), post.getViews(), post.getLikes() - 1);
    postRepo.save(updated);

    Map<String, Object> result = new LinkedHashMap<>();
    result.put("post_id", postId);
    result.put("likes_count", likeRepo.countLikes(postId));
    return result;
  }

  // 좋아요 조회
  public Map<String, Object> getLikes(Long postId, Long userId) {
    postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    long count = likeRepo.countLikes(postId);
    boolean liked = (userId != null) && likeRepo.hasLiked(userId, postId);

    Map<String, Object> result = new LinkedHashMap<>();
    result.put("post_id", postId);
    result.put("likes_count", count);
    result.put("liked_by_me", liked);
    return result;
  }
}
