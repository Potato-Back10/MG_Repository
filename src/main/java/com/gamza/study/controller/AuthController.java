package com.gamza.study.controller;

import com.gamza.study.dto.LoginDto;
import com.gamza.study.dto.requestDto.UserLoginRequestDto;
import com.gamza.study.dto.requestDto.UserSignupRequestDto;
import com.gamza.study.dto.responseDto.TokenResponseDto;
import com.gamza.study.dto.responseDto.UserResponseDto;
import com.gamza.study.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserSignupRequestDto userSignupRequestDto) {
        return ResponseEntity.ok(userService.signup(userSignupRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@CookieValue("refreshToken") String refreshToken) {
        TokenResponseDto tokenResponseDto = userService.reissue(refreshToken);

        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto,
                                                 HttpServletResponse httpServletResponse) {
        LoginDto loginDto = userService.login(userLoginRequestDto, httpServletResponse);
        return ResponseEntity.ok(loginDto);

    }
}
