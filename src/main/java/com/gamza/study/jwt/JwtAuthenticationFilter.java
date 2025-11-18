package com.gamza.study.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtParser jwtProvider;
    private final AuthenticationService authenticationService;
    private final WhiteListMatcher whiteListMatcher;
    private final List<String> whiteList;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 화이트리스트면 통과
        if(whiteListMatcher.isWhiteListed(path, whiteList)) {
            filterChain.doFilter(request, response);
            return;
        }

        //jwt 추출
        String jwt = request.getHeader("Authorization");

        if (jwt == null || !jwt.startsWith(JwtConstants.TOKEN_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT token missing or invalid");
            return;
        }

        String token = jwt.substring(JwtConstants.TOKEN_PREFIX_LENGTH);

        //jwt 파싱
        Claims claims = jwtProvider.parseClaims(token);

        //인증 생성
        Authentication authentication = authenticationService.createAuthentication(claims);

        //SecurityContext 세팅
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);    // 필터 체인의 다음 필터 호출
    }
}
