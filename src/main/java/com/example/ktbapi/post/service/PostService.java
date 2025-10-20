package com.example.ktbapi.post.service;

import com.example.ktbapi.common.TimeUtil;
import com.example.ktbapi.post.dto.*;
import com.example.ktbapi.post.mapper.PostMapper;
import com.example.ktbapi.post.model.Comment;
import com.example.ktbapi.post.model.Post;
import com.example.ktbapi.post.repo.CommentRepository;
import com.example.ktbapi.post.repo.LikeRepository;
import com.example.ktbapi.post.repo.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

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

  // ===== 게시글 목록 (Map -> DTO) =====
  public List<PostSummaryResponse> getAllPosts(int page, int limit, String sort) {
    List<Post> posts = postRepo.findAll();
    if ("date".equalsIgnoreCase(sort)) Collections.reverse(posts);

    int start = Math.max(0, (page - 1) * limit);
    int end = Math.min(start + limit, posts.size());
    List<Post> paged = posts.subList(start, end);

    return paged.stream().map(PostMapper::toSummary).toList();
  }

  // ===== 게시글 상세 (조회수 +1) (Map -> DTO) =====
  public PostDetailResponse getPostById(Long id) {
    Post post = postRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    Post updated = new Post(
        post.getId(), post.getTitle(), post.getContent(), post.getAuthor(),
        post.getImageUrl(), post.getCreatedAt(), post.getViews() + 1, post.getLikes()
    );
    postRepo.save(updated);

    List<Comment> comments = commentRepo.findAllByPostId(id);
    return PostMapper.toDetail(updated, comments);
  }

  // ===== 게시글 생성 (Map -> DTO) =====
  public PostDetailResponse createPost(Long userId, String authorName, PostCreateRequest req) {
    if (req == null || req.title == null || req.content == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    String now = TimeUtil.nowText();
    Post saved = postRepo.save(new Post(
        null, req.title, req.content, authorName, req.image_url, now, 0, 0
    ));
    // 생성 시점엔 댓글 없음
    return PostMapper.toDetail(saved, List.of());
  }

  // ===== 게시글 수정 (Map -> DTO) =====
  public PostUpdatedResponse updatePost(Long userId, Long postId, PostUpdateRequest req) {
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

    return new PostUpdatedResponse(
        patched.getId(), patched.getTitle(), patched.getContent(),
        patched.getImageUrl(), TimeUtil.nowText()
    );
  }

  // ===== 댓글 생성 (Map -> DTO) =====
  public CommentResponse createComment(Long userId, Long postId, String authorName, CommentCreateOrUpdateRequest req) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
    if (req == null || req.content == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");

    postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    String now = TimeUtil.nowText();
    Comment saved = commentRepo.addComment(postId, new Comment(null, authorName, req.content, now));

    return new CommentResponse(saved.getId(), postId, saved.getAuthor(), saved.getContent(), saved.getCreatedAt());
  }

  // ===== 댓글 수정 (Map -> DTO) =====
  public CommentUpdatedResponse updateComment(Long userId, Long postId, Long commentId, CommentCreateOrUpdateRequest req) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
    if (req == null || req.content == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");

    postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));
    Comment c = commentRepo.findComment(postId, commentId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment_not_found"));

    c.setContent(req.content);
    return new CommentUpdatedResponse(c.getId(), TimeUtil.nowText());
  }

  // ===== 댓글 삭제 =====
  public void deleteComment(Long userId, Long postId, Long commentId) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    boolean ok = commentRepo.deleteComment(postId, commentId);
    if (!ok) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "comment_not_found");
  }

  // ===== 좋아요 추가 (Map -> DTO) =====
  public LikeCountResponse addLike(Long userId, Long postId) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    Post post = postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    boolean added = likeRepo.addLike(userId, postId);
    if (!added)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "already_liked");

    Post updated = new Post(
        post.getId(), post.getTitle(), post.getContent(), post.getAuthor(),
        post.getImageUrl(), post.getCreatedAt(), post.getViews(), post.getLikes() + 1
    );
    postRepo.save(updated);

    long count = likeRepo.countLikes(postId);
    return new LikeCountResponse(postId, count);
  }

  // ===== 좋아요 취소 (Map -> DTO) =====
  public LikeCountResponse removeLike(Long userId, Long postId) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    Post post = postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    boolean removed = likeRepo.removeLike(userId, postId);
    if (!removed)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not_liked");

    Post updated = new Post(
        post.getId(), post.getTitle(), post.getContent(), post.getAuthor(),
        post.getImageUrl(), post.getCreatedAt(), post.getViews(), post.getLikes() - 1
    );
    postRepo.save(updated);

    long count = likeRepo.countLikes(postId);
    return new LikeCountResponse(postId, count);
  }

  // ===== 좋아요 조회 (Map -> DTO) =====
  public LikeStatusResponse getLikes(Long postId, Long userId) {
    postRepo.findById(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));

    long count = likeRepo.countLikes(postId);
    boolean liked = (userId != null) && likeRepo.hasLiked(userId, postId);
    return new LikeStatusResponse(postId, count, liked);
  }
}
