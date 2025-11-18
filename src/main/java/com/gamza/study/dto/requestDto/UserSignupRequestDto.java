package com.gamza.study.dto.requestDto;

public record UserSignupRequestDto(
        String username,
        String email,
        String password
) {}
