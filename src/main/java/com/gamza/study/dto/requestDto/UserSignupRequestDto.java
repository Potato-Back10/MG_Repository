package com.gamza.study.dto.requestDto;

import com.gamza.study.entity.enums.Gender;

import java.util.Date;

public record UserSignupRequestDto(
        String loginId,
        String password,
        String username,
        String phoneNumber,
        Gender gender,
        Date birthDate
) {}
