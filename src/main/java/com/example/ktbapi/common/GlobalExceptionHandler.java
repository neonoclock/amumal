package com.example.ktbapi.common;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<Void>> handle(ResponseStatusException e){
    String reason = e.getReason() == null ? e.getStatusCode().toString() : e.getReason();
  
    return ResponseEntity
            .status(e.getStatusCode())               
            .body(ApiResponse.fail(reason));         
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleOther(Exception e){

    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.fail("internal_server_error"));
  }
}