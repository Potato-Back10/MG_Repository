package com.gamza.study.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private int status;
    private String message;
    private String code;
    private LocalDateTime timestamp;

    @Setter
    private Map<String, String> fieldErrors;

    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.code = errorCode.getErrorCode();
        this.timestamp = LocalDateTime.now();
    }
}
