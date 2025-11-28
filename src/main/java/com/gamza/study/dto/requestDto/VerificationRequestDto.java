package com.gamza.study.dto.requestDto;

public record VerificationRequestDto(
        String phoneNumber,
        String code
) {}
