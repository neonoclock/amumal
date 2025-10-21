package com.example.ktbapi.common;

import com.example.ktbapi.common.exception.NotFoundException;
import com.example.ktbapi.common.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.example.ktbapi")
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static ResponseEntity<ApiResponse<?>> build(HttpStatus status, String code, Object detail) {
        return ResponseEntity.status(status).body(ApiResponse.error(code, detail));
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(UnauthorizedException ex) {
        String code = ex.getMessage() != null ? ex.getMessage() : "unauthorized";
        return build(HttpStatus.UNAUTHORIZED, code, null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(NotFoundException ex) {
        String code = ex.getMessage() != null ? ex.getMessage() : "not_found";
        return build(HttpStatus.NOT_FOUND, code, null);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        log.debug("❌ Validation error: {}", ex.getMessage());
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, "invalid_request", fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleNotReadable(HttpMessageNotReadableException ex) {
        log.debug("❌ JSON parse error: {}", ex.getMessage());
        return build(
                HttpStatus.BAD_REQUEST,
                "invalid_json",
                ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.debug("❌ Type mismatch: parameter={}, value={}, requiredType={}",
                ex.getName(), ex.getValue(), ex.getRequiredType());
        Map<String, Object> detail = Map.of(
                "name", ex.getName(),
                "requiredType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : null,
                "value", ex.getValue()
        );
        return build(HttpStatus.BAD_REQUEST, "type_mismatch", detail);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandler(NoHandlerFoundException ex) {
        log.info("⚠️ No handler found: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        Map<String, Object> detail = Map.of(
                "path", ex.getRequestURL(),
                "httpMethod", ex.getHttpMethod()
        );
        return build(HttpStatus.NOT_FOUND, "not_found", detail);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResource(NoResourceFoundException ex) {
        String path = ex.getResourcePath();

        if (path == null || path.contains("/swagger-ui") || path.contains("/v3/api-docs") ||
            path.contains("/webjars") || path.contains("/favicon")) {
            log.trace("Swagger/static resource bypassed: {}", path);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        log.info("⚠️ Resource not found: {} {}", ex.getHttpMethod(), path);
        Map<String, Object> detail = Map.of(
                "path", path,
                "httpMethod", ex.getHttpMethod()
        );
        return build(HttpStatus.NOT_FOUND, "not_found", detail);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.debug("❌ Method not allowed: {}, supported={}", ex.getMethod(), ex.getSupportedHttpMethods());
        Map<String, Object> detail = Map.of(
                "method", ex.getMethod(),
                "supported", ex.getSupportedHttpMethods()
        );
        return build(HttpStatus.METHOD_NOT_ALLOWED, "method_not_allowed", detail);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiResponse<?>> handleNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        log.debug("❌ Media type not acceptable: {}", ex.getMessage());
        return build(HttpStatus.NOT_ACCEPTABLE, "not_acceptable", ex.getMessage());
    }
}
