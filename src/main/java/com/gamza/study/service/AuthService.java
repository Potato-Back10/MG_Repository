package com.gamza.study.service;

import com.gamza.study.dto.LoginDto;
import com.gamza.study.dto.requestDto.UserLoginRequestDto;
import com.gamza.study.dto.requestDto.UserSignupRequestDto;
import com.gamza.study.dto.responseDto.TokenResponseDto;
import com.gamza.study.dto.responseDto.UserResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserResponseDto signup(UserSignupRequestDto userSignupRequestDto);
    TokenResponseDto reissue(String refreshToken);
    LoginDto login(UserLoginRequestDto userLoginRequestDto, HttpServletResponse httpServletResponse);
}
