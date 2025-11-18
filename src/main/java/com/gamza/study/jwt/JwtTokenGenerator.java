package com.gamza.study.jwt;

import com.gamza.study.entity.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenGenerator {

    private final SecretKey secretKey;
    private final Long accessExpMs;
    private final Long refreshExpMs;
    private final Clock clock;

    public JwtTokenGenerator(
            SecretKey secretKey,
            @Value("${jwt.token.access-expiration-time}") Long accessExpMs,
            @Value("${jwt.token.refresh-expiration-time}") Long refreshExpMs,
            Clock clock) {
        this.secretKey = secretKey;
        this.accessExpMs = accessExpMs;
        this.refreshExpMs = refreshExpMs;
        this.clock = clock;
    }

    public String createAccessToken(long userId, String email, Role role) {
        Instant now = Instant.now(clock);
        Instant expiration = now.plusMillis(accessExpMs);

        // claim
        Claims claims = Jwts.claims();
        claims.put(JwtConstants.CLAIM_USER_ID, userId);
        claims.put(JwtConstants.CLAIM_EMAIL, email);
        if (role != null) {
            claims.put(JwtConstants.CLAIM_ROLE, role.toString());
        }

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .addClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();

    }

    public String createRefreshToken(long userId) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(refreshExpMs);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .addClaims(Map.of(JwtConstants.CLAIM_USER_ID, userId))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }
}
