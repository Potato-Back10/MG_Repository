package com.gamza.study.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationService {

    public Authentication createAuthentication(Claims claims) {
        long userId = ((Number) claims.get(JwtConstants.CLAIM_USER_ID)).longValue();
        String email = (String) claims.get(JwtConstants.CLAIM_EMAIL);
        String role = (String) claims.get(JwtConstants.CLAIM_ROLE);

        // SecurityContext 에 추가할 Authentication 인스턴스 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role != null && !role.isBlank()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        CustomUserDetails userDetails = new CustomUserDetails(userId, email, role, authorities);

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

    }
}
