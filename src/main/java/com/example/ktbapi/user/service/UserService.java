package com.example.ktbapi.user.service;

import com.example.ktbapi.common.dto.LoginResponse;
import com.example.ktbapi.user.dto.LoginRequest;
import com.example.ktbapi.user.dto.PasswordUpdateRequest;
import com.example.ktbapi.user.dto.ProfileUpdateRequest;
import com.example.ktbapi.user.dto.SignupRequest;

public interface UserService {

    void signup(SignupRequest req);

    LoginResponse login(LoginRequest req);

    void updateProfile(Long userId, ProfileUpdateRequest req);

    void updatePassword(Long userId, PasswordUpdateRequest req);

    void deleteUser(Long userId);
}

