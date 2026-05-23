package com.liushipin.userorderauthsystem.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 简易 JWT 工具类
 *
 * 当前版本特点：
 * 1. 不依赖第三方 JWT 库
 * 2. 使用 HMAC-SHA256 签名
 * 3. token 中保存 userId 和 username
 *
 * 注意：
 * 这是学习项目可用的简化版本。
 * 后续正式项目可以换成 jjwt 或 Spring Security。
 */
public class JwtUtil {

    /**
     * 签名密钥
     * 注意：
     * 真实项目不要写死在代码里，应该放到配置文件或环境变量。
     */
    private static final String SECRET = "user-order-auth-secret-key";

    /**
     * token 有效期：24 小时
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000L;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 生成 token
     */
    public static String generateToken(Long userId, String username) {
        try {
            // JWT 头部
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            // JWT 载荷
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", userId);
            payload.put("username", username);
            payload.put("expireTime", System.currentTimeMillis() + EXPIRE_TIME);

            // Base64Url 编码
            String headerBase64 = base64UrlEncode(objectMapper.writeValueAsString(header));
            String payloadBase64 = base64UrlEncode(objectMapper.writeValueAsString(payload));

            // 待签名内容
            String content = headerBase64 + "." + payloadBase64;

            // 签名
            String signature = hmacSha256(content, SECRET);

            return content + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("生成 token 失败", e);
        }
    }

    /**
     * 解析 token，获取 userId
     */
    public static Long getUserId(String token) {
        Map<String, Object> payload = parsePayload(token);
        Object userId = payload.get("userId");

        if (userId == null) {
            throw new RuntimeException("token 中不存在 userId");
        }

        return Long.valueOf(userId.toString());
    }

    /**
     * 校验 token 是否有效
     */
    public static void validateToken(String token) {
        try {
            String[] parts = token.split("\\.");

            if (parts.length != 3) {
                throw new RuntimeException("token 格式错误");
            }

            String content = parts[0] + "." + parts[1];
            String signature = hmacSha256(content, SECRET);

            // 校验签名是否一致
            if (!signature.equals(parts[2])) {
                throw new RuntimeException("token 签名错误");
            }

            Map<String, Object> payload = parsePayload(token);
            Long expireTime = Long.valueOf(payload.get("expireTime").toString());

            // 校验是否过期
            if (System.currentTimeMillis() > expireTime) {
                throw new RuntimeException("token 已过期");
            }
        } catch (Exception e) {
            throw new RuntimeException("token 无效：" + e.getMessage());
        }
    }

    /**
     * 解析 JWT payload
     */
    private static Map<String, Object> parsePayload(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            return objectMapper.readValue(payloadJson, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("解析 token 失败", e);
        }
    }

    /**
     * Base64Url 编码
     */
    private static String base64UrlEncode(String content) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * HMAC-SHA256 签名
     */
    private static String hmacSha256(String content, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);

        byte[] signBytes = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(signBytes);
    }
}