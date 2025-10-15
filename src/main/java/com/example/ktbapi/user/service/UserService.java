package com.example.ktbapi.user.service;

import com.example.ktbapi.user.dto.*;
import com.example.ktbapi.user.model.User;
import com.example.ktbapi.user.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserService {
  private final UserRepository repo;

  public UserService(UserRepository repo){ this.repo = repo; }

  // 로그인
  public Map<String, Object> login(LoginRequest req){
    if(req==null || req.email==null || req.password==null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");

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

  // 회원가입
  public void signup(SignupRequest req){
    if(req==null || req.email==null || req.password==null || req.password_check==null || req.nickname==null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");
    if(!req.password.equals(req.password_check))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");
    if(repo.existsByEmailOrNickname(req.email, req.nickname))
      throw new ResponseStatusException(HttpStatus.CONFLICT, "duplicate_email_or_nickname");

    repo.save(new User(null, req.email, req.password, req.nickname, req.profile_image));
  }

  // 프로필 수정
  public Map<String,Object> updateProfile(Long userId, ProfileUpdateRequest req){
    User user = repo.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized"));

    if(req.nickname != null && !req.nickname.equalsIgnoreCase(user.getNickname())){
      if(repo.existsByNicknameExceptId(req.nickname, user.getId()))
        throw new ResponseStatusException(HttpStatus.CONFLICT, "duplicate_nickname");
      user.setNickname(req.nickname);
    }

    if(req.profile_image != null){
      user.setProfileImage(req.profile_image);
    }

    repo.save(user);
    String updatedAt = LocalDateTime.now().withNano(0).toString().replace('T',' ');
    return Map.of(
      "user_id", user.getId(),
      "nickname", user.getNickname(),
      "profile_image", user.getProfileImage(),
      "updated_at", updatedAt
    );
  }

  // 비밀번호 수정
  public void updatePassword(Long userId, PasswordUpdateRequest req){
    if(req==null || req.current_password==null || req.new_password==null || req.new_password_check==null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");

    User user = repo.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized"));

    if(!user.getPassword().equals(req.current_password))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");

    if(!req.new_password.equals(req.new_password_check))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid_request");

    repo.save(new User(user.getId(), user.getEmail(), req.new_password, user.getNickname(), user.getProfileImage()));
  }
}
