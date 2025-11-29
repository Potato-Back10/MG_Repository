package com.gamza.study.config;

import org.springframework.stereotype.Component;

@Component
public class SecurityWhiteList {
    public static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",

            "/auth/signup",
            "/auth/login",
            "/auth/reissue",

            "/api/auth/check_login_id",

            "/api/sms/send",
            "/api/sms/verify"
    };

}
