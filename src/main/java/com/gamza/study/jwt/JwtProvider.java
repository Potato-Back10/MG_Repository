package com.gamza.study.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtParser jwtParser;

    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims = jwtParser.parseClaims(token);
        return claims.get(JwtConstants.CLAIM_USER_ID, Long.class);
    }

    public String getEmail(String token) {
        Claims claims = jwtParser.parseClaims(token);
        return claims.get(JwtConstants.CLAIM_EMAIL, String.class);
    }

}
