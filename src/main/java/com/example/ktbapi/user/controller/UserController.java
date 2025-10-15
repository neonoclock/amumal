package com.example.ktbapi.user.controller;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.user.dto.*;
import com.example.ktbapi.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService service;

  public UserController(UserService s){ this.service = s; }

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest req){
    var data = service.login(req);
    return ResponseEntity.ok(ApiResponse.ok("login_success", data));
  }

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignupRequest req){
    service.signup(req);
    return ResponseEntity.status(201).body(ApiResponse.ok("signup_success", null));
  }

  // 프로필 수정
  @PatchMapping("/me")
  public ResponseEntity<ApiResponse<?>> updateProfile(@RequestHeader("X-User-Id") Long userId,
                                                      @RequestBody ProfileUpdateRequest req){
    var data = service.updateProfile(userId, req);
    return ResponseEntity.ok(ApiResponse.ok("profile_update_success", data));
  }

  // 비밀번호 변경
  @PatchMapping("/me/password")
  public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestHeader("X-User-Id") Long userId,
                                                          @RequestBody PasswordUpdateRequest req){
    service.updatePassword(userId, req);
    return ResponseEntity.ok(ApiResponse.ok("password_update_success", null));
  }
}
