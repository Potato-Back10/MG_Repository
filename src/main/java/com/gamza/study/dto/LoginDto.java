package com.gamza.study.dto;

public record LoginDto(
        String token,
        boolean isNewUser
) {}
