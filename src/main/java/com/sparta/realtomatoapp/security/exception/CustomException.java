package com.sparta.realtomatoapp.security.exception;

import com.sparta.realtomatoapp.security.exception.eunm.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;
}
