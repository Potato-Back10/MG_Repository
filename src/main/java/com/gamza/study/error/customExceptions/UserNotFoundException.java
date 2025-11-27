package com.gamza.study.error.customExceptions;

import com.gamza.study.error.ErrorCode;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
  private final ErrorCode errorCode;

  public UserNotFoundException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}

