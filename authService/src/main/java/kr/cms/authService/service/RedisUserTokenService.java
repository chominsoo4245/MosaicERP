package kr.cms.authService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisUserTokenService {
    private final StringRedisTemplate redisTemplate;
    private static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofDays(30);
    private static final String REFRESH_PREFIX = "refresh:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public String saveRefreshToken(String username){
        String uuid = UUID.randomUUID().toString();
        String key = REFRESH_PREFIX + uuid;
        redisTemplate.opsForValue().set(key, username, REFRESH_TOKEN_EXPIRATION);
        return uuid;
    }

    public String getUsernameFromRefreshToken(String uuid){
        return redisTemplate.opsForValue().get(REFRESH_PREFIX + uuid);
    }

    public void deleteRefreshToken(String uuid){
        redisTemplate.delete(REFRESH_PREFIX + uuid);
    }

    public void addToBlacklist(String accessToken, long expiration){
        String key = BLACKLIST_PREFIX + accessToken;
        redisTemplate.opsForValue().set(key, "logout", Duration.ofSeconds(expiration));
    }

    public boolean isBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + accessToken));
    }
}
