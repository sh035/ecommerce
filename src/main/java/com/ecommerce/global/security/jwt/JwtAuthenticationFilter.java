package com.ecommerce.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final JwtRedisService jwtRedisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String accessToken = tokenProvider.resolveToken(request);
        if (StringUtils.hasText(accessToken)) {
            if (tokenProvider.validateToken(accessToken)) {
                setAuthentication(accessToken);
            } else {
                handleExpiredAccessToken(request, response, accessToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleExpiredAccessToken(HttpServletRequest request,
        HttpServletResponse response, String oldAccessToken) {

        Authentication authentication = tokenProvider.getAuthentication(oldAccessToken);
        String memberId = tokenProvider.getMemberIdByAccessToken(oldAccessToken);
        long refreshTokenExpiration = tokenProvider.getExpiration(
            jwtRedisService.findRefreshTokenByAccessToken(oldAccessToken));

        if (refreshTokenExpiration < 0) {
            jwtRedisService.deleteRefreshTokenByAccessToken(oldAccessToken);
            SecurityContextHolder.clearContext();

            return;
        }

        JwtTokenDto token = tokenProvider.generateToken(memberId, authentication.getAuthorities());

        jwtRedisService.deleteRefreshTokenByAccessToken(oldAccessToken);
        jwtRedisService.save(token, refreshTokenExpiration);

        setAuthentication(token.getAccessToken());
        response.setHeader("access-token", token.getAccessToken());
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
