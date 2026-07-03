package com.liushipin.userorderauthsystem.security;

import com.liushipin.userorderauthsystem.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties properties;
    private final SecretKey secretKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, String username) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(properties.getExpiration());

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(String token) {
        Object userId = parseClaims(token).get("userId");
        if (userId == null) {
            throw new IllegalArgumentException("JWT does not contain userId");
        }
        return Long.valueOf(userId.toString());
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Instant getExpiresAt(String token) {
        return parseClaims(token).getExpiration().toInstant();
    }
}
