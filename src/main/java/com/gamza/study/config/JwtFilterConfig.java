package com.gamza.study.config;

import com.gamza.study.jwt.AuthenticationService;
import com.gamza.study.jwt.JwtAuthenticationFilter;
import com.gamza.study.jwt.JwtParser;
import com.gamza.study.jwt.WhiteListMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class JwtFilterConfig {

    private final JwtParser jwtParser;
    private final AuthenticationService authenticationService;
    private final WhiteListMatcher whiteListMatcher;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
                jwtParser,
                authenticationService,
                whiteListMatcher,
                Arrays.asList(SecurityWhiteList.AUTH_WHITELIST));
    }
}
