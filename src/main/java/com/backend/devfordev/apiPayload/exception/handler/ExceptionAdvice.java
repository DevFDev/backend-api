package com.backend.devfordev.apiPayload.exception.handler;

import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.apiPayload.code.BaseErrorCode;
import com.backend.devfordev.apiPayload.code.ErrorReasonDTO;
import com.backend.devfordev.apiPayload.code.status.ErrorStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// GlobalException
@Slf4j
@ControllerAdvice
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {


    /**
     * [Exception] 유효성 검사 실패
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Validation Exception: ", ex);

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Invalid value");
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest()
                .body(ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), "Validation failed", errors));
    }

    /**
     * [Exception] Enum 값이 유효하지 않음
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal Argument Exception: ", ex);
        return ResponseEntity.badRequest()
                .body(ApiResponse.onFailure(ErrorStatus.INVALID_ENUM_VALUE.getCode(), ex.getMessage(), null));
    }

    /**
     * [Exception] 타입 불일치
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Type Mismatch Exception: ", ex);
        String errorMessage = String.format("Invalid value '%s' for parameter '%s'. Expected type is '%s'.",
                ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());
        return ResponseEntity.badRequest()
                .body(ApiResponse.onFailure(ErrorStatus.INVALID_TYPE.getCode(), errorMessage, null));
    }

    /**
     * [Exception] 잘못된 형식으로 인해 요청 본문을 읽을 수 없음
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("HttpMessageNotReadableException: ", ex);

        String errorMessage = "Invalid request body format. Please check the JSON structure and values.";
        Throwable cause = ex.getCause();

        // InvalidFormatException을 직접 처리
        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            com.fasterxml.jackson.databind.exc.InvalidFormatException invalidFormatException =
                    (com.fasterxml.jackson.databind.exc.InvalidFormatException) cause;

            // Enum 값에 대한 오류인지 확인
            if (invalidFormatException.getTargetType().isEnum()) {
                errorMessage = String.format("Invalid value '%s' for enum field. Accepted values are: %s",
                        invalidFormatException.getValue(),
                        Arrays.toString(invalidFormatException.getTargetType().getEnumConstants()));
            }
        }

        ApiResponse<?> response = ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), errorMessage, null);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * [Exception] ConstraintViolationException 처리
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint Violation Exception: ", ex);

        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + " - " + violation.getMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest()
                .body(ApiResponse.onFailure(ErrorStatus.INVALID_REQUEST.getCode(), errorMessage, null));
    }

    /**
     * [Exception] Expired JWT
     */
    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ApiResponse<?>> handleExpiredJwtException(ExpiredJwtException ex) {
        log.error("Expired JWT Exception: ", ex);

        // 안전한 메시지 파싱
        String originalMessage = ex.getMessage();
        String expiredAtUtc = "";
        String currentAtUtc = "";

        try {
            expiredAtUtc = originalMessage.substring(originalMessage.indexOf("at") + 3, originalMessage.indexOf("Z.") + 1);
            currentAtUtc = originalMessage.substring(originalMessage.indexOf("time:") + 6, originalMessage.indexOf("Z,", originalMessage.indexOf("time:")) + 1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX", Locale.US);
            ZonedDateTime expiredAtKst = ZonedDateTime.parse(expiredAtUtc, formatter).plusHours(9);
            ZonedDateTime currentAtKst = ZonedDateTime.parse(currentAtUtc, formatter).plusHours(9);

            originalMessage = originalMessage
                    .replace(expiredAtUtc, expiredAtKst.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX")))
                    .replace(currentAtUtc, currentAtKst.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX")));
        } catch (Exception e) {
            log.warn("JWT parsing failed, using original message.");
        }

        return ResponseEntity.badRequest()
                .body(ApiResponse.onFailure(ErrorStatus.EXPIRED_JWT_ERROR.getCode(), originalMessage, null));
    }

    /**
     * [Exception] 기타
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception ex) {
        log.error("Unhandled Exception: ", ex);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.onFailure(ErrorStatus._INTERNAL_SERVER_ERROR.getCode(), ex.getMessage(), null));
    }





    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity onThrowException(GeneralException generalException, HttpServletRequest request) {
        ErrorReasonDTO errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
        return handleExceptionInternal(generalException,errorReasonHttpStatus,null,request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorReasonDTO reason,
                                                           HttpHeaders headers, HttpServletRequest request) {

        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(),reason.getMessage(),null);
//        e.printStackTrace();

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                reason.getHttpStatus(),
                webRequest
        );
    }


}
