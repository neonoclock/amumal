package com.example.ktbapi.common;

public class ApiResponse<T>{          
  private final String message;
  private final T data;

  private ApiResponse(String m, T d){
    this.message = m;
    this.data = d;
  }

  public static <T> ApiResponse<T> ok(String m, T d){
    return new ApiResponse<>(m, d);
  }

  public static <T> ApiResponse<T> fail(String m){
    return new ApiResponse<>(m, null);
  }

  public String getMessage(){
    return message;
  }

  public T getData(){
    return data;
  }
}
