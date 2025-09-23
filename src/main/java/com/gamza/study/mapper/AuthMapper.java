package com.gamza.study.mapper;

import com.gamza.study.dto.requestDto.UserSignupRequestDto;
import com.gamza.study.dto.responseDto.UserResponseDto;
import com.gamza.study.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    default UserEntity toEntity(UserSignupRequestDto dto, String encodedPassword) {
        return UserEntity.builder()
                .username(dto.username())
                .email(dto.email())
                .password(encodedPassword)
                .build();
    }

    UserResponseDto toResponseDto(UserEntity entity);
}
