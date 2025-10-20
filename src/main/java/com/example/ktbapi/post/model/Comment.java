package com.example.ktbapi.post.model;

import java.util.Objects;

public class Comment {
    private final Long id;
    private final String author;
    private String content;        // ← 외부 세터 제거, 도메인 메서드로만 변경
    private final String createdAt;

    public Comment(Long id, String author, String content, String createdAt) {
        if (author == null || author.isBlank()) throw new IllegalArgumentException("author required");
        if (content == null || content.isBlank()) throw new IllegalArgumentException("content required");
        if (createdAt == null || createdAt.isBlank()) throw new IllegalArgumentException("createdAt required");

        this.id = id;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }

    // ===== 도메인 동작(행위) =====
    public void updateContent(String newContent) {
        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("content required");
        }
        this.content = newContent;
    }

    // ===== 접근자 =====
    public Long getId() { return id; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }

    // 동일성: id 기준
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment that = (Comment) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return "Comment{id=" + id + ", author='" + author + "', createdAt='" + createdAt + "'}";
    }
}
