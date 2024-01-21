package kr.co.promptech.noticeboard.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static kr.co.promptech.noticeboard.constants.Constants.REFRESH_TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(String userId, String refreshToken, long expiration) {
        String key = buildRefreshTokenKey(userId);
        redisTemplate.opsForValue().set(key, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String userId) {
        String key = buildRefreshTokenKey(userId);
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(String userId) {
        String key = buildRefreshTokenKey(userId);
        redisTemplate.delete(key);
    }

    private String buildRefreshTokenKey(String userId) {
        return REFRESH_TOKEN_PREFIX + userId;
    }
}
