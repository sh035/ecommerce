package com.ecommerce.global.security.jwt;

import com.ecommerce.global.security.jwt.exception.InvalidTokenException;
import com.ecommerce.global.security.auth.PrincipalDetailService;
import com.ecommerce.global.security.auth.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${spring.jwt.secret_key}")
    private String secretKey;

    @Value("${spring.jwt.access.expiration}")
    private Long accessExpiration;

    @Value("${spring.jwt.refresh.expiration}")
    private Long refreshExpiration;

    private Key key;

    private final PrincipalDetailService principalDetailService;
    private final JwtRedisService jwtRedisService;

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


    public JwtTokenDto generateToken(String memberId,
        Collection<? extends GrantedAuthority> authorities) {

        long now = (new Date()).getTime();

        String accessToken = Jwts.builder()
            .setSubject(memberId)
            .claim("auth", authorities)
            .setExpiration(new Date(now + accessExpiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        String refreshToken = Jwts.builder()
            .setSubject(memberId)
            .setExpiration(new Date(now + refreshExpiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return JwtTokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public Authentication getAuthentication(String token) {

        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailService.loadUserByUsername(
            getMemberIdByAccessToken(token));

        return new UsernamePasswordAuthenticationToken(principalDetails, null,
            principalDetails.getAuthorities());


    }

    public boolean validateToken(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
            return !jwtRedisService.hasKeyBlackList(accessToken) && !claims.getExpiration()
                .before(new Date());
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않은 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("토큰이 비어있습니다.");
        } catch (SignatureException e) {
            log.error("잘못된 서명의 토큰입니다.");
        }

        return false;
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(PREFIX)) {
            return token.substring(PREFIX.length());
        }

        return null;
    }

    public Long getExpiration(String token) {
        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();

        return expiration.getTime() - new Date().getTime();
    }

    public String getMemberIdByAccessToken(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }


}
