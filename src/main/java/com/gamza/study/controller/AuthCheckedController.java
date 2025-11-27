package com.gamza.study.controller;

import com.gamza.study.dto.requestDto.CheckLoginIdRequestDto;
import com.gamza.study.dto.responseDto.CheckLoginIdResponseDto;
import com.gamza.study.service.AuthCheckedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthCheckedController {
    private final AuthCheckedService authCheckedService;

    @PostMapping("/check_login_id")
    public ResponseEntity<CheckLoginIdResponseDto> checkedLoginId(
            @RequestBody CheckLoginIdRequestDto checkLoginIdRequestDto
            ) {
        boolean exists = authCheckedService.isLoginIdDuplicate(checkLoginIdRequestDto.loginId());

        return ResponseEntity.ok(new CheckLoginIdResponseDto(exists));
    }
}
