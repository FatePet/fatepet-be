package com.fatepet.petrest.global.exception;

import com.fatepet.petrest.global.response.ApiResponse;
import com.fatepet.petrest.global.response.ResponseCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FuneralBusinessException.class)
    public ResponseEntity<ApiResponse<Void>> FuneralBusinessExceptionHandler(FuneralBusinessException e){
        ApiResponse<Void> response = ApiResponse.of(e.getStatusCode(), e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    @ExceptionHandler(SmsException.class)
    public ResponseEntity<ApiResponse<Void>> handleSmsException(SmsException e){
        System.out.println(e.getMessage());
        System.out.println(e.getStatusCode());
        ApiResponse<Void> response = ApiResponse.of(e.getStatusCode(), e.getMessage());
        System.out.println(response);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiResponse<Void> response = ApiResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingRequestParam(MissingServletRequestParameterException e) {
        String message = String.format("필수 파라미터 '%s'가 누락되었습니다.", e.getParameterName());
        ApiResponse<Void> response = ApiResponse.of(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception e) {
        ApiResponse<Void> response = ApiResponse.of(ResponseCode.INTERNAL_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
