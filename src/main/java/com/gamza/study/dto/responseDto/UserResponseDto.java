package com.gamza.study.dto.responseDto;

import com.gamza.study.entity.enums.Role;

public record UserResponseDto(
   Long id,
   String username,
   String email,
   Role role
) {}
