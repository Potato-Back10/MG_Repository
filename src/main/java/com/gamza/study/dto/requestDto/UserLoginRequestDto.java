package com.gamza.study.dto.requestDto;

public record UserLoginRequestDto(
        String email,
        String password
) {}
