package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TokenBlacklistServiceTest {

    @Test
    void shouldStoreHashedTokenWithRemainingJwtLifetime() {
        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
        @SuppressWarnings("unchecked")
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        TokenBlacklistService service = new TokenBlacklistService(redisTemplate);
        String token = JwtUtil.generateToken(1L, "lisi");

        service.blacklist(token);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Duration> ttlCaptor = ArgumentCaptor.forClass(Duration.class);
        verify(valueOperations).set(keyCaptor.capture(), anyString(), ttlCaptor.capture());
        assertTrue(keyCaptor.getValue().startsWith("auth:token:blacklist:"));
        assertTrue(!keyCaptor.getValue().contains(token));
        assertTrue(ttlCaptor.getValue().compareTo(Duration.ofHours(23)) > 0);
        assertTrue(ttlCaptor.getValue().compareTo(Duration.ofHours(24)) <= 0);
    }
}
