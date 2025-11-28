package com.gamza.study.controller;

import com.gamza.study.dto.requestDto.SmsSendRequestDto;
import com.gamza.study.dto.requestDto.VerificationRequestDto;
import com.gamza.study.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "문자 전송 by 조민기")
    @PostMapping("/send")
    public ResponseEntity<String> sendAuthCode(
            @RequestBody SmsSendRequestDto smsSendRequestDto
    ){
        messageService.sendAuthCode(smsSendRequestDto.phoneNumber());

        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }

    @Operation(summary = "인증번호 검증 by 조민기")
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(
            @RequestBody VerificationRequestDto verifyRequest
    ) {
        boolean result = messageService.verifyAuthCode(verifyRequest.phoneNumber(), verifyRequest.code());

        return ResponseEntity.ok(result ? "인증 성공" : "인증 실패");
    }

}
