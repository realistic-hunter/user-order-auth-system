package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.config.AppRedisProperties;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoginAttemptServiceTest {

    @Test
    void shouldIncrementAndSetTtlWithOneAtomicScript() {
        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
        AppRedisProperties properties = new AppRedisProperties();
        properties.setLoginFailureWindow(Duration.ofMinutes(15));
        LoginAttemptService service = new LoginAttemptService(redisTemplate, properties);

        service.recordFailure(" LiSi ");

        verify(redisTemplate).execute(
                org.mockito.ArgumentMatchers.<RedisScript<Long>>any(),
                eq(List.of("auth:login:failure:lisi")),
                eq("900000")
        );
    }
}
