package com.example.ktbapi.post.model;

public class Post {
  private Long id;
  private String title;
  private String content;
  private String author;
  private String imageUrl;
  private String createdAt;
  private int views;
  private int likes;

  public Post(Long id, String title, String content, String author,
              String imageUrl, String createdAt, int views, int likes) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
    this.imageUrl = imageUrl;
    this.createdAt = createdAt;
    this.views = views;
    this.likes = likes;
  }

  public Long getId(){ return id; }
  public String getTitle(){ return title; }
  public String getContent(){ return content; }
  public String getAuthor(){ return author; }
  public String getImageUrl(){ return imageUrl; }
  public String getCreatedAt(){ return createdAt; }
  public int getViews(){ return views; }
  public int getLikes(){ return likes; }
}
