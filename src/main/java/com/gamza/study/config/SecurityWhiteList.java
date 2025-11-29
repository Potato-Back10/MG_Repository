package com.gamza.study.config;

import org.springframework.stereotype.Component;

@Component
public class SecurityWhiteList {
    public static final String[] AUTH_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-config",
            "/auth/signup",
            "/auth/login",
            "/auth/reissue",
            "/api/auth/check_login_id"
    };

}
