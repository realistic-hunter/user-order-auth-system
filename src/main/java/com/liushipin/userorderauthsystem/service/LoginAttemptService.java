package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.config.AppRedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Locale;

@Service
public class LoginAttemptService {

    private static final Logger log = LoggerFactory.getLogger(LoginAttemptService.class);
    private static final String KEY_PREFIX = "auth:login:failure:";
    private static final DefaultRedisScript<Long> RECORD_FAILURE_SCRIPT = new DefaultRedisScript<>(
            """
            local failures = redis.call('INCR', KEYS[1])
            if failures == 1 then
                redis.call('PEXPIRE', KEYS[1], ARGV[1])
            end
            return failures
            """,
            Long.class
    );

    private final StringRedisTemplate redisTemplate;
    private final AppRedisProperties redisProperties;

    public LoginAttemptService(StringRedisTemplate redisTemplate, AppRedisProperties redisProperties) {
        this.redisTemplate = redisTemplate;
        this.redisProperties = redisProperties;
    }

    public boolean isBlocked(String username) {
        try {
            String value = redisTemplate.opsForValue().get(key(username));
            return value != null && Long.parseLong(value) >= redisProperties.getMaxLoginFailures();
        } catch (DataAccessException | NumberFormatException e) {
            log.warn("Failed to read login failure counter, username={}", username, e);
            return false;
        }
    }

    public void recordFailure(String username) {
        try {
            redisTemplate.execute(
                    RECORD_FAILURE_SCRIPT,
                    Collections.singletonList(key(username)),
                    String.valueOf(redisProperties.getLoginFailureWindow().toMillis())
            );
        } catch (DataAccessException e) {
            log.warn("Failed to record login failure, username={}", username, e);
        }
    }

    public void clear(String username) {
        try {
            redisTemplate.delete(key(username));
        } catch (DataAccessException e) {
            log.warn("Failed to clear login failure counter, username={}", username, e);
        }
    }

    private String key(String username) {
        return KEY_PREFIX + username.trim().toLowerCase(Locale.ROOT);
    }
}
