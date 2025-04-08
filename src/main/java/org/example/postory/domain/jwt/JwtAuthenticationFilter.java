package org.example.postory.domain.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.auth.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtTokenProvider;
    private static final String[] WHITELIST = {"/", "/api/users/signup", "/api/auth/login"};

    /**
     * 1. Request Header에서 JWT 토큰 추출
     * 2. validateToken으로 토큰 유효성 검사
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        if (!isLoginCheck(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        String token = resolveToken((HttpServletRequest) request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    /**
     * Request Header에서 토큰 정보 추출
     */

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 화이트 리스트는 인증 체크 x
     */
    private boolean isLoginCheck(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITELIST, requestURI);
    }
}
