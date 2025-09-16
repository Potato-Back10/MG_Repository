package com.gamza.study.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 공통 에러 (COMMON)
    NOT_FOUND(404, "COMMON-404", "요청한 리소스를 찾을 수 없습니다"),
    INTER_SERVER_ERROR(500, "COMMON-500", "서버 내부 오류가 발생했습니다"),
    METHOD_ARGUMENT_NOT_VALID(400, "COMMON-400", "잘못된 요청 파라미터입니다"),

    // 인증/인가 에러 (AUTH)
    UNAUTHORIZED(401, "AUTH-401", "인증이 필요합니다"),
    UNAUTHORIZED_ACTION(403, "AUTH-403", "해당 작업에 권한이 없습니다."),
    INVALID_REFRESH_TOKEN(401, "AUTH-401", "유효하지 않은 리프레시 토큰입니다"),
    INVALID_ACCESS_TOKEN(401, "AUTH-401", "유효하지 않은 액세스 토큰입니다"),
    TOKEN_EXPIRED(401, "AUTH-401", "토큰이 만료되었습니다"),
    INVALID_PASSWORD(401, "AUTH-401", "잘못된 비밀번호입니다"),

    // 사용자 관련 에러 (USER)
    USER_NOT_FOUND(404, "USER-404", "사용자를 찾을 수 없습니다"),
    DUPLICATE_USER(409, "USER-409", "이미 존재하는 사용자입니다"),
    DUPLICATE_EMAIL(409, "USER-409", "이미 등록된 이메일입니다. 다른 이메일을 사용해주세요"),
    INVALID_EMAIL_VERIFICATION_CODE(401, "USER-401", "유효하지 않은 이메일 인증 코드입니다"),
    INVALID_FRIEND_CODE(404, "USER-404", "유효하지 않은 친구 코드입니다."),
    EMAIL_CODE_TIMEOUT(410, "USER-410", "이메일 인증 코드가 만료되었습니다"),
    EMAIL_VERIFICATION_REQUEST_NOT_FOUND(400, "USER-400", "이메일 인증 요청을 찾을 수 없습니다"),

    //잔액 관련 에러
    INSUFFICIENT_BALANCE(400, "INSUFFICIENT-400", "잔액이 부족합니다."),
    INSUFFICIENT_COIN_BALANCE(400, "INSUFFICIENT-400", "코인 잔액이 부족합니다."),

    //주문 관련 에러
    ORDER_NOT_FOUND(404, "ORDER-404", "주문을 찾을 수 없습니다."),
    INVALID_ORDER_STATE(400, "ORDER-400","주문이 이미 체결되었거나 취소되어 취소 할 수 없습니다.");

    private int status;
    private String errorCode;
    private String message;
}
