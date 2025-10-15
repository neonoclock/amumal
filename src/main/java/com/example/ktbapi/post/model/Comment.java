package com.example.ktbapi.post.model;

public class Comment {
  private Long id;
  private String author;
  private String content;
  private String createdAt;

  public Comment(Long id, String author, String content, String createdAt) {
    this.id = id;
    this.author = author;
    this.content = content;
    this.createdAt = createdAt;
  }

  public Long getId() { return id; }
  public String getAuthor() { return author; }
  public String getContent() { return content; }
  public void setContent(String content) { this.content = content; }
  public String getCreatedAt() { return createdAt; }
}
