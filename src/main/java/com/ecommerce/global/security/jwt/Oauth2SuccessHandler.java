package com.ecommerce.global.security.jwt;

import com.ecommerce.global.security.auth.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final JwtRedisService jwtRedisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        String memberId = ((PrincipalDetails) authentication.getPrincipal()).getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        JwtTokenDto token = tokenProvider.generateToken(memberId, authorities);
        long refreshTokenExpiration = tokenProvider.getExpiration(token.getRefreshToken());

        jwtRedisService.save(token, refreshTokenExpiration);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"accessToken\": \"" + token.getAccessToken() + "\"}");
        response.getWriter().flush();
    }
}
