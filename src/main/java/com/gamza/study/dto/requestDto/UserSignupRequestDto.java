package com.gamza.study.dto.requestDto;

import com.gamza.study.entity.enums.Role;

public record UserSignupRequestDto(
        String username,
        String email,
        String password
) {}
