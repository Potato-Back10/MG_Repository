package com.gamza.study.controller;

import com.gamza.study.dto.requestDto.CheckLoginIdRequestDto;
import com.gamza.study.dto.responseDto.CheckLoginIdResponseDto;
import com.gamza.study.service.AuthCheckedService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthCheckedController {
    private final AuthCheckedService authCheckedService;

    @Operation(summary = "아이디 중복확인 by 조민기")
    @PostMapping("/check_login_id")
    public ResponseEntity<CheckLoginIdResponseDto> checkedLoginId(
            @RequestBody CheckLoginIdRequestDto checkLoginIdRequestDto
            ) {
        boolean exists = authCheckedService.isLoginIdDuplicate(checkLoginIdRequestDto.loginId());

        return ResponseEntity.ok(new CheckLoginIdResponseDto(exists));
    }
}
