package com.gamza.study.service;

import com.gamza.study.dto.LoginDto;
import com.gamza.study.dto.requestDto.UserLoginRequestDto;
import com.gamza.study.dto.requestDto.UserSignupRequestDto;
import com.gamza.study.dto.responseDto.TokenResponseDto;
import com.gamza.study.entity.UserEntity;
import com.gamza.study.entity.enums.Role;
import com.gamza.study.error.ErrorCode;
import com.gamza.study.error.customExceptions.UnauthorizedException;
import com.gamza.study.error.customExceptions.UserNotFoundException;
import com.gamza.study.jwt.JwtProvider;
import com.gamza.study.jwt.JwtTokenGenerator;
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
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtTokenGenerator jwtTokenGenerator;

    @Override
    public void signup(UserSignupRequestDto userSignupRequestDto) {
        String loginId = userSignupRequestDto.loginId();

        if (userRepository.existsByLoginId(loginId)) {
            throw new UserNotFoundException(ErrorCode.DUPLICATE_USER);
        }

        String encodedPassword = passwordEncoder.encode(userSignupRequestDto.password());
        UserEntity userEntity = authMapper.toEntity(userSignupRequestDto, encodedPassword);
        userRepository.save(userEntity);

    }

    @Override
    public TokenResponseDto reissue(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtProvider.getUserId(refreshToken);
        String email = jwtProvider.getEmail(refreshToken);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);

        Role role = null;
        if (userEntity != null) {
            role = userEntity.getRole();
        }

        String newAccessToken = jwtTokenGenerator.createAccessToken(userId, email, role);
        return new TokenResponseDto(newAccessToken);
    }

    @Value("${jwt.token.refresh-expiration-time}")
    private Long refreshExpMs;

    @Override
    public LoginDto login(UserLoginRequestDto userLoginRequestDto,HttpServletResponse httpServletResponse) {
        String loginId = userLoginRequestDto.loginId();
        String password = userLoginRequestDto.password();

        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
        }

        long userId = userEntity.getId();
        String username = userEntity.getUsername();
        Role role = userEntity.getRole();

        String accessToken = jwtTokenGenerator.createAccessToken(userId, username, role);
        String refreshToken = jwtTokenGenerator.createRefreshToken(userId);

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
