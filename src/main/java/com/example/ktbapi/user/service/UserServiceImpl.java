package com.example.ktbapi.user.service;

import com.example.ktbapi.common.TimeUtil;
import com.example.ktbapi.common.dto.LoginResponse;
import com.example.ktbapi.common.exception.InvalidRequestException;
import com.example.ktbapi.common.exception.NotFoundException;
import com.example.ktbapi.common.exception.UnauthorizedException;
import com.example.ktbapi.user.dto.LoginRequest;
import com.example.ktbapi.user.dto.PasswordUpdateRequest;
import com.example.ktbapi.user.dto.ProfileUpdateRequest;
import com.example.ktbapi.user.dto.SignupRequest;
import com.example.ktbapi.user.model.User;
import com.example.ktbapi.user.repo.UserRepository;
import org.springframework.stereotype.Service;
import com.example.ktbapi.common.auth.RequireUserId;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository repo;

  public UserServiceImpl(UserRepository repo) {
    this.repo = repo;
  }

  @Override
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

  @Override
  public void signup(SignupRequest req) {
    if (repo.existsByEmailOrNickname(req.email, req.nickname)) {
      throw new InvalidRequestException("duplicate_user");
    }
    repo.save(new User(null, req.email, req.password, req.nickname, TimeUtil.nowText()));
  }


  @RequireUserId(paramIndex = 0)
  @Override
  public void updateProfile(Long userId, ProfileUpdateRequest req) {
      var u = repo.findById(userId).orElseThrow(NotFoundException::new);
      u.changeNickname(req.nickname);
      repo.save(u);
  }

  @RequireUserId(paramIndex = 0)
  @Override
  public void updatePassword(Long userId, PasswordUpdateRequest req) {
      var u = repo.findById(userId).orElseThrow(NotFoundException::new);
      if (!u.getPassword().equals(req.current_password)) throw new UnauthorizedException();
      u.changePassword(req.new_password);
      repo.save(u);
  }

  @RequireUserId(paramIndex = 0)
  @Override
  public void deleteUser(Long userId) {
      repo.deleteById(userId);
  }
}
