package com.example.ktbapi.post.model;

import java.util.Objects;

public final class LikeRecord {
    private final Long userId;
    private final Long postId;
    private final String createdAt;

    public LikeRecord(Long userId, Long postId, String createdAt) {
        if (userId == null) throw new IllegalArgumentException("userId required");
        if (postId == null) throw new IllegalArgumentException("postId required");
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    public static LikeRecord keyOf(Long userId, Long postId) {
        return new LikeRecord(userId, postId, null);
    }

    public Long getUserId() { return userId; }
    public Long getPostId() { return postId; }
    public String getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LikeRecord)) return false;
        LikeRecord that = (LikeRecord) o;
        return Objects.equals(userId, that.userId)
            && Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }

    @Override
    public String toString() {
        return "LikeRecord{userId=" + userId + ", postId=" + postId + ", createdAt='" + createdAt + "'}";
    }
}
