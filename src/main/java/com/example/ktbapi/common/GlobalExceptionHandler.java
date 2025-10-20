package com.example.ktbapi.common;

import com.example.ktbapi.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.ktbapi.common.Msg.Error;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static ResponseEntity<ApiResponse<?>> build(HttpStatus status, String code, Object detail) {
        return ResponseEntity.status(status).body(ApiResponse.ok(code, detail));
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlePostNotFound(PostNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, Error.POST_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleCommentNotFound(CommentNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, Error.COMMENT_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidRequest(InvalidRequestException ex) {
        return build(HttpStatus.BAD_REQUEST, Error.INVALID_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(UnauthorizedException ex) {
        return build(HttpStatus.UNAUTHORIZED, Error.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AlreadyLikedException.class)
    public ResponseEntity<ApiResponse<?>> handleAlreadyLiked(AlreadyLikedException ex) {
        return build(HttpStatus.BAD_REQUEST, Error.ALREADY_LIKED, ex.getMessage());
    }

    @ExceptionHandler(NotLikedException.class)
    public ResponseEntity<ApiResponse<?>> handleNotLiked(NotLikedException ex) {
        return build(HttpStatus.BAD_REQUEST, Error.NOT_LIKED, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, Error.INVALID_REQUEST, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleOthers(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, Error.INTERNAL_ERROR, ex.getMessage());
    }
}
