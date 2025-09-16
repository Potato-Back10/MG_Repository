package com.gamza.study.error.customExceptions;

import com.gamza.study.error.ErrorCode;
import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {
    private final ErrorCode errorCode;

    public OrderException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
