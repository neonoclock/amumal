package com.example.ktbapi.user.model;

public class User {
  private Long id;
  private String email;
  private String password;
  private String nickname;
  private String profileImage;

  // 기본 생성자
  public User(Long id, String email, String password, String nickname) {
    this(id, email, password, nickname, null);
  }

  // 모든 필드를 초기화하는 생성자
  public User(Long id, String email, String password, String nickname, String profileImage) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.profileImage = profileImage;
  }

  // Getter 메서드
  public Long getId(){ return id; }
  public String getEmail(){ return email; }
  public String getPassword(){ return password; }
  public String getNickname(){ return nickname; }
  public String getProfileImage(){ return profileImage; }

  // Setter 메서드
  public void setNickname(String nickname){ this.nickname = nickname; }
  public void setProfileImage(String profileImage){ this.profileImage = profileImage; }
}
