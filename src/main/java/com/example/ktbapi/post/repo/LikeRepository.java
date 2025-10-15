package com.example.ktbapi.post.repo;

import com.example.ktbapi.post.model.LikeRecord;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LikeRepository {
  // 좋아요 저장 (userId, postId) 조합
  private final Set<LikeRecord> likes = ConcurrentHashMap.newKeySet();

  public boolean addLike(Long userId, Long postId) {
    return likes.add(new LikeRecord(userId, postId)); // true면 새로 추가
  }

  public boolean removeLike(Long userId, Long postId) {
    return likes.remove(new LikeRecord(userId, postId)); // true면 성공적으로 제거
  }

  public long countLikes(Long postId) {
    return likes.stream().filter(l -> l.getPostId().equals(postId)).count();
  }

  public boolean hasLiked(Long userId, Long postId) {
    return likes.contains(new LikeRecord(userId, postId));
  }
}
