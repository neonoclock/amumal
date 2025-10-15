package com.example.ktbapi.post.model;

public class LikeRecord {
  private final Long userId;
  private final Long postId;

  public LikeRecord(Long userId, Long postId) {
    this.userId = userId;
    this.postId = postId;
  }

  public Long getUserId() { return userId; }
  public Long getPostId() { return postId; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LikeRecord)) return false;
    LikeRecord other = (LikeRecord) o;
    return userId.equals(other.userId) && postId.equals(other.postId);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(userId, postId);
  }
}
