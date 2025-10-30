package com.gamza.study.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private SecretKey secretKey;

    public JwtAuthenticationFilter(@Value("${jwt.secret}") String signingKey) {
        this.secretKey = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        // 특정 경로를 필터에서 제외
        if (path.equals("/auth/login")
                || path.equals("/auth/signup")
                || path.equals("/auth/reissue")
                || pathMatcher.match("/swagger-ui.html", path)
                || pathMatcher.match("/swagger-ui/**", path)
                || pathMatcher.match("/v3/api-docs/**", path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader("Authorization");

        if (jwt == null || !jwt.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT token missing or invalid");
            return;
        }

        // 'Bearer ' 접두어를 제거하고 실제 JWT 토큰만 가져오기
        jwt = jwt.substring(7);

        // 토큰에서 클레임을 얻고 서명 검증.
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        Number userIdNumber = (Number) claims.get("userId");
        Long userId = userIdNumber.longValue();
        String email = (String) claims.get("email");
        String role = (String) claims.get("role");

        // SecurityContext 에 추가할 Authentication 인스턴스 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role != null && !role.isBlank()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        CustomUserDetails userDetails = new CustomUserDetails(userId, claims.getSubject(), email, authorities);

        UsernamePasswordAuthentication authentication = new UsernamePasswordAuthentication(userDetails, null, authorities);

        // SecurityContext 에 Authentication 객체 추가
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        filterChain.doFilter(request, response);    // 필터 체인의 다음 필터 호출
    }
}
