package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.time.Instant;

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
        JwtService jwtService = mock(JwtService.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        TokenBlacklistService service = new TokenBlacklistService(redisTemplate, jwtService);
        String token = "jwt-token";
        when(jwtService.getExpiresAt(token)).thenReturn(Instant.now().plus(Duration.ofHours(24)));

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
