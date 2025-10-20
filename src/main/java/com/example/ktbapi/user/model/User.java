package com.example.ktbapi.user.model;

import java.util.Objects;

public class User {
    private final Long id;
    private final String email;
    private String password;     // 변경은 도메인 메서드로만
    private String nickname;     // 변경은 도메인 메서드로만
    private final String createdAt;

    public User(Long id, String email, String password, String nickname, String createdAt) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email required");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("password required");
        if (nickname == null || nickname.isBlank()) throw new IllegalArgumentException("nickname required");
        if (createdAt == null || createdAt.isBlank()) throw new IllegalArgumentException("createdAt required");

        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    // ===== 도메인 동작(행위) =====
    public void changeNickname(String newNickname) {
        if (newNickname == null || newNickname.isBlank()) {
            throw new IllegalArgumentException("nickname required");
        }
        this.nickname = newNickname;
    }

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("password required");
        }
        // 필요 시 정책(길이/해시) 적용
        this.password = newPassword;
    }

    // ===== 접근자 =====
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public String getCreatedAt() { return createdAt; }

    // 동일성: id 기준
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return "User{id=" + id + ", email='" + email + "', nickname='" + nickname + "'}";
    }
}
