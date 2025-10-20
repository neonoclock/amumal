package com.example.ktbapi.user.service;

import com.example.ktbapi.common.TimeUtil;
import com.example.ktbapi.user.dto.LoginRequest;
import com.example.ktbapi.user.dto.PasswordUpdateRequest;
import com.example.ktbapi.user.dto.ProfileUpdateRequest;
import com.example.ktbapi.user.dto.SignupRequest;
import com.example.ktbapi.user.model.User;
import com.example.ktbapi.user.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class UserService {
  private final UserRepository repo;

  public UserService(UserRepository repo){ this.repo = repo; }

  public Map<String, Object> login(LoginRequest req){
    User u = repo.findByEmail(req.email)
        .filter(x -> x.getPassword().equals(req.password))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized"));

    return Map.of(
      "user_id", u.getId(),
      "token_type", "Bearer",
      "access_token", "JWT_ACCESS_TOKEN",
      "refresh_token", "JWT_REFRESH_TOKEN",
      "expires_in", 3600
    );
  }

  public Long signup(SignupRequest req) {
    User saved = repo.save(new User(
        null, req.email, req.password, req.nickname, TimeUtil.nowText() // ✅ 변경
    ));
    return saved.getId();
  }

  public void updateProfile(Long userId, ProfileUpdateRequest req) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    User u = repo.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user_not_found"));

    u.changeNickname(req.nickname);
    repo.save(u);
  }

  public void updatePassword(Long userId, PasswordUpdateRequest req) {
    if (userId == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    User u = repo.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user_not_found"));

    if (!u.getPassword().equals(req.current_password)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid_current_password");
    }

    u.changePassword(req.new_password);
    repo.save(u);
  }
}
