package com.gamza.study.dto.requestDto;

public record UserLoginRequestDto(
        String loginId,
        String password
) {}
