package com.liushipin.userorderauthsystem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushipin.userorderauthsystem.config.AppRedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionCacheService {

    private static final Logger log = LoggerFactory.getLogger(PermissionCacheService.class);
    private static final String KEY_PREFIX = "auth:permission:user:";
    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<>() {
    };

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final AppRedisProperties redisProperties;

    public PermissionCacheService(StringRedisTemplate redisTemplate,
                                  ObjectMapper objectMapper,
                                  AppRedisProperties redisProperties) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.redisProperties = redisProperties;
    }

    public Optional<List<String>> get(Long userId) {
        try {
            String value = redisTemplate.opsForValue().get(key(userId));
            if (value == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(value, STRING_LIST_TYPE));
        } catch (DataAccessException e) {
            log.warn("Redis unavailable while reading permission cache, userId={}", userId, e);
            return Optional.empty();
        } catch (Exception e) {
            log.warn("Invalid permission cache value, userId={}", userId, e);
            evict(userId);
            return Optional.empty();
        }
    }

    public void put(Long userId, List<String> permissionCodes) {
        try {
            String value = objectMapper.writeValueAsString(permissionCodes);
            redisTemplate.opsForValue().set(
                    key(userId),
                    value,
                    redisProperties.getPermissionCacheTtl()
            );
        } catch (Exception e) {
            log.warn("Failed to write permission cache, userId={}", userId, e);
        }
    }

    public void evict(Long userId) {
        try {
            redisTemplate.delete(key(userId));
        } catch (DataAccessException e) {
            log.warn("Failed to evict permission cache, userId={}", userId, e);
        }
    }

    private String key(Long userId) {
        return KEY_PREFIX + userId;
    }
}
