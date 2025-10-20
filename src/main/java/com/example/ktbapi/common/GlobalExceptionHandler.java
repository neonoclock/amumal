package com.example.ktbapi.common;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private Map<String, Object> body(String message, Object data) {
        return Map.of("message", message, "data", data);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return body("invalid_request", errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, Object> handleConstraint(ConstraintViolationException ex) {
        return body("invalid_request", ex.getMessage());
    }

    // 서비스에서 ResponseStatusException을 던진 경우 (상태/사유 보존)
    @ExceptionHandler(ResponseStatusException.class)
    public Map<String, Object> handleRSE(ResponseStatusException ex) {
        log.error("ResponseStatusException: {}", ex.getReason(), ex);
        // 상태코드는 예외에 들어있으므로 그대로 반영됨
        return body(ex.getReason() != null ? ex.getReason() : "error", null);
    }

    // 마지막 방파제: 예측 못한 모든 예외
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleUnknown(Exception ex) {
        log.error("Unexpected error", ex); // 콘솔에 전체 스택트레이스
        // 로컬 디버깅이라 메시지를 응답에 포함 (운영에선 null 권장)
        return body("internal_server_error", ex.getMessage());
    }
}
