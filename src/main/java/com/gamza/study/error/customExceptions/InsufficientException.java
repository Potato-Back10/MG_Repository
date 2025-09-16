package com.gamza.study.error.customExceptions;

import com.gamza.study.error.ErrorCode;
import lombok.Getter;

@Getter
public class InsufficientException extends RuntimeException {
    private final ErrorCode errorCode;

    public InsufficientException (ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
