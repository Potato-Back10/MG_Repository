package com.gamza.study.error;

import com.gamza.study.error.customExceptions.InsufficientException;
import com.gamza.study.error.customExceptions.OrderException;
import com.gamza.study.error.customExceptions.UnauthorizedException;
import com.gamza.study.error.customExceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Validation error occurred for request: {}", ex.getParameter().getParameterName(), ex);

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Map<String, String> errorMap = new HashMap<>();

        for (FieldError fieldError : fieldErrors) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse response = new ErrorResponse(ErrorCode.METHOD_ARGUMENT_NOT_VALID);
        response.setFieldErrors(errorMap);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 리소스 없음 예외 처리
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 잘못된 인수 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument provided: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.METHOD_ARGUMENT_NOT_VALID);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 인증 실패 예외 처리
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        log.warn("Unauthorized access: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 사용자 관련 예외 처리
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //잔고 관련 예외 처리
    @ExceptionHandler(InsufficientException.class)
    public ResponseEntity<ErrorResponse> InsufficientException(InsufficientException ex) {
        log.error("Insufficient balance: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    //Order 관련 예외 처리
    @ExceptionHandler(InsufficientException.class)
    public ResponseEntity<ErrorResponse> OrderException(OrderException ex) {
        log.error("Order not found: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

}
