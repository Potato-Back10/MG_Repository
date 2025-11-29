package com.gamza.study.mapper;

import com.gamza.study.dto.requestDto.UserSignupRequestDto;
import com.gamza.study.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    default UserEntity toEntity(UserSignupRequestDto dto, String encodedPassword) {
        return UserEntity.builder()
                .loginId(dto.loginId())
                .password(encodedPassword)
                .username(dto.username())
                .phoneNumber(dto.phoneNumber())
                .gender(dto.gender())
                .birthDate(dto.birthDate())
                .verify(dto.vertified())
                .build();
    }

}
