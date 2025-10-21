package com.example.ktbapi.user.service;

import com.example.ktbapi.common.TimeUtil;
import com.example.ktbapi.common.dto.LoginResponse;
import com.example.ktbapi.common.exception.NotFoundException;
import com.example.ktbapi.common.exception.UnauthorizedException;
import com.example.ktbapi.user.dto.LoginRequest;
import com.example.ktbapi.user.dto.PasswordUpdateRequest;
import com.example.ktbapi.user.dto.ProfileUpdateRequest;
import com.example.ktbapi.user.dto.SignupRequest;
import com.example.ktbapi.user.model.User;
import com.example.ktbapi.user.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository repo;

  public UserService(UserRepository repo) {
    this.repo = repo;
  }

  public LoginResponse login(LoginRequest req) {
    User u = repo.findByEmail(req.email)
        .filter(x -> x.getPassword().equals(req.password))
        .orElseThrow(UnauthorizedException::new);

    return new LoginResponse(
        u.getId(),
        "Bearer",
        "JWT_ACCESS_TOKEN",
        "JWT_REFRESH_TOKEN",
        3600
    );
  }


  public Long signup(SignupRequest req) {
    User saved = repo.save(new User(
        null, req.email, req.password, req.nickname, TimeUtil.nowText()
    ));
    return saved.getId();
  }

  public void updateProfile(Long userId, ProfileUpdateRequest req) {
    if (userId == null) throw new UnauthorizedException();

    User u = repo.findById(userId).orElseThrow(NotFoundException::new);

    u.changeNickname(req.nickname);
    repo.save(u);
  }

  public void updatePassword(Long userId, PasswordUpdateRequest req) {
    if (userId == null) throw new UnauthorizedException();

    User u = repo.findById(userId).orElseThrow(NotFoundException::new);

    if (!u.getPassword().equals(req.current_password)) {
      throw new UnauthorizedException();
    }

    u.changePassword(req.new_password);
    repo.save(u);
  }
}
