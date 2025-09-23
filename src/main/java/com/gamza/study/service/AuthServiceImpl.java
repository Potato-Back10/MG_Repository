package com.gamza.study.service;

import com.gamza.study.dto.LoginDto;
import com.gamza.study.dto.requestDto.UserLoginRequestDto;
import com.gamza.study.dto.requestDto.UserSignupRequestDto;
import com.gamza.study.dto.responseDto.TokenResponseDto;
import com.gamza.study.dto.responseDto.UserResponseDto;
import com.gamza.study.entity.UserEntity;
import com.gamza.study.entity.enums.Role;
import com.gamza.study.error.ErrorCode;
import com.gamza.study.error.customExceptions.UnauthorizedException;
import com.gamza.study.error.customExceptions.UserNotFoundException;
import com.gamza.study.jwt.JwtUtil;
import com.gamza.study.mapper.AuthMapper;
import com.gamza.study.repository.AuthRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository userRepository;
    private final AuthMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDto signup(UserSignupRequestDto userSignupRequestDto) {
        String email = userSignupRequestDto.email();

        if (userRepository.existsByEmail(email)) {
            throw new UserNotFoundException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(userSignupRequestDto.password());
        UserEntity userEntity = userMapper.toEntity(userSignupRequestDto, encodedPassword);
        userRepository.save(userEntity);

        return userMapper.toResponseDto(userEntity);
    }

    @Override
    public TokenResponseDto reissue(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtUtil.getUserId(refreshToken);
        String email = jwtUtil.getEmail(refreshToken);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);

        Role role = null;
        if (userEntity != null) {
            role = userEntity.getRole();
        }

        String newAccessToken = jwtUtil.createAccessToken(userId, email, role);
        return new TokenResponseDto(newAccessToken);
    }

    @Value("${jwt.token.refresh-expiration-time}")
    private Long refreshExpMs;

    @Override
    public LoginDto login(UserLoginRequestDto userLoginRequestDto,HttpServletResponse httpServletResponse) {
        String email = userLoginRequestDto.email();
        String password = userLoginRequestDto.password();

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
        }

        Long userId = userEntity.getId();
        Role role = userEntity.getRole();

        String accessToken = jwtUtil.createAccessToken(userId, email, role);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        httpServletResponse.setHeader("Authorization", "Bearer " + accessToken);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(refreshExpMs)
                .sameSite("None")
                .build();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new LoginDto(accessToken, false);

    }
}
