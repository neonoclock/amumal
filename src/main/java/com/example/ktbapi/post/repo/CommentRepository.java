package com.example.ktbapi.post.repo;

import com.example.ktbapi.post.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CommentRepository {
  // postId -> comments
  private final Map<Long, List<Comment>> store = new HashMap<>();
  private long seq = 0L;

  public synchronized Comment addComment(Long postId, Comment comment) {
    Comment saved = new Comment(++seq, comment.getAuthor(), comment.getContent(), comment.getCreatedAt());
    store.computeIfAbsent(postId, k -> new ArrayList<>()).add(saved);
    return saved;
  }

  public Optional<Comment> findComment(Long postId, Long commentId) {
    List<Comment> list = store.get(postId);
    if (list == null) return Optional.empty();
    return list.stream().filter(c -> c.getId().equals(commentId)).findFirst();
  }

  public boolean deleteComment(Long postId, Long commentId) {
    List<Comment> list = store.get(postId);
    if (list == null) return false;
    return list.removeIf(c -> c.getId().equals(commentId));
  }

  public List<Comment> findAllByPostId(Long postId) {
    return store.getOrDefault(postId, Collections.emptyList());
  }
}
