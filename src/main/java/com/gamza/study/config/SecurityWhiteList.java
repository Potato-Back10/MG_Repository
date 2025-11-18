package com.gamza.study.config;

import org.springframework.stereotype.Component;

@Component
public class SecurityWhiteList {
    public static final String[] AUTH_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs/**",
            "/auth/signup",
            "/auth/login",
            "/auth/reissue"
    };

}
