package com.example.ktbapi.post.model;

import java.util.Objects;

public class Comment {
    private final Long id;
    private final String author;
    private String content;
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
    
    public void updateContent(String newContent) {
        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("content required");
        }
        this.content = newContent;
    }

    public Long getId() { return id; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }

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
