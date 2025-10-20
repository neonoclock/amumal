package com.example.ktbapi.user.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 요청")
public class SignupRequest {
  @Email(message = "invalid email format")
  @NotBlank(message = "email is required")
  public String email;

  @NotBlank(message = "password is required")
  @Size(min = 8, message = "password must be >= 8 chars")
  public String password;

  @NotBlank(message = "password_check is required")
  public String password_check;

  @NotBlank(message = "nickname is required")
  @Size(max = 30, message = "nickname must be <= 30 chars")
  public String nickname;

  public String profile_image;

  @AssertTrue(message = "password_mismatch")
  public boolean isPasswordMatch() {
    return password != null && password.equals(password_check);
  }
}
