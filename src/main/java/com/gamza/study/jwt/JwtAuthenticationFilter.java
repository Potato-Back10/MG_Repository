package com.gamza.study.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final List<String> whiteList;

    public JwtAuthenticationFilter(SecretKey secretKey, String[] whiteList) {
        this.secretKey = secretKey;
        this.whiteList = List.of(whiteList);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        for(String pattern : whiteList) {
            if(pathMatcher.match(pattern, path)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String jwt = request.getHeader("Authorization");

        if (jwt == null || !jwt.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT token missing or invalid");
            return;
        }

        // 'Bearer ' 접두어를 제거하고 실제 JWT 토큰만 가져오기
        jwt = jwt.substring(JwtConstants.TOKEN_PREFIX_LENGTH);

        // 토큰에서 클레임을 얻고 서명 검증.
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        long userId = ((Number) claims.get(JwtConstants.CLAIM_USER_ID)).longValue();
        String email = (String) claims.get(JwtConstants.CLAIM_EMAIL);
        String role = (String) claims.get(JwtConstants.CLAIM_ROLE);


        // SecurityContext 에 추가할 Authentication 인스턴스 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role != null && !role.isBlank()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        CustomUserDetails userDetails = new CustomUserDetails(userId, email, role, authorities);


        UsernamePasswordAuthentication authentication = new UsernamePasswordAuthentication(userDetails, null, authorities);

        // SecurityContext 에 Authentication 객체 추가
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        filterChain.doFilter(request, response);    // 필터 체인의 다음 필터 호출
    }
}
