package com.example.ktbapi.user.controller;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.user.dto.*;
import com.example.ktbapi.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService service;

  public UserController(UserService s){ this.service = s; }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest req){
    var data = service.login(req);
    return ResponseEntity.ok(ApiResponse.ok("login_success", data));
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest req){
    service.signup(req);
    return ResponseEntity.status(201).body(ApiResponse.ok("signup_success", null));
  }

  @PatchMapping("/me")
  public ResponseEntity<ApiResponse<Void>> updateProfile(@RequestHeader("X-User-Id") Long userId,
                                                         @Valid @RequestBody ProfileUpdateRequest req){
    service.updateProfile(userId, req);
    return ResponseEntity.ok(ApiResponse.ok("profile_update_success", null));
  }

  @PatchMapping("/me/password")
  public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestHeader("X-User-Id") Long userId,
                                                          @Valid @RequestBody PasswordUpdateRequest req){
    service.updatePassword(userId, req);
    return ResponseEntity.ok(ApiResponse.ok("password_update_success", null));
  }
}
