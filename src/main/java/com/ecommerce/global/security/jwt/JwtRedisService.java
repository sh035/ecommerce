package com.ecommerce.global.security.jwt;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JwtRedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public JwtRedisService(
        @Qualifier("jwtRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void save(JwtTokenDto jwtTokenDto, long refreshTokenExpiration) {

        redisTemplate.opsForValue()
            .set("accessToken:" + jwtTokenDto.getAccessToken(), jwtTokenDto.getRefreshToken()
            , refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public String findRefreshTokenByAccessToken(String accessToken) {
        return redisTemplate.opsForValue().get("accessToken:" + accessToken);
    }

    public void deleteRefreshTokenByAccessToken(String accessToken) {
        redisTemplate.delete("accessToken:" + accessToken);
    }

    public void setBlackList(String accessToken, String memberId, Long accessTokenExpiration) {
        redisTemplate.opsForValue()
            .set("blackList:" + accessToken, memberId, accessTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public boolean hasKeyBlackList(String accessToken) {
        return redisTemplate.hasKey("blackList:" + accessToken);
    }
}
