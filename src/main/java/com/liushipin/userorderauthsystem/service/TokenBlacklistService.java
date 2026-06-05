package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.util.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;

@Service
public class TokenBlacklistService {

    private static final String KEY_PREFIX = "auth:token:blacklist:";

    private final StringRedisTemplate redisTemplate;

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklist(String token) {
        long remainingMillis = JwtUtil.getExpireTime(token) - System.currentTimeMillis();
        if (remainingMillis > 0) {
            redisTemplate.opsForValue().set(
                    key(token),
                    "1",
                    Duration.ofMillis(remainingMillis)
            );
        }
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key(token)));
    }

    private String key(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return KEY_PREFIX + HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
