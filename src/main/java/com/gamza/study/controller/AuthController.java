package com.gamza.study.controller;

import com.gamza.study.dto.LoginDto;
import com.gamza.study.dto.requestDto.UserLoginRequestDto;
import com.gamza.study.dto.requestDto.UserSignupRequestDto;
import com.gamza.study.dto.responseDto.TokenResponseDto;
import com.gamza.study.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @RequestBody UserSignupRequestDto userSignupRequestDto) {

        authService.signup(userSignupRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(
            @CookieValue("refreshToken") String refreshToken) {

        TokenResponseDto tokenResponseDto = authService.reissue(refreshToken);

        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(
            @RequestBody UserLoginRequestDto userLoginRequestDto,
            HttpServletResponse httpServletResponse) {

        LoginDto loginDto = authService.login(userLoginRequestDto, httpServletResponse);
        return ResponseEntity.ok(loginDto);

    }
}
