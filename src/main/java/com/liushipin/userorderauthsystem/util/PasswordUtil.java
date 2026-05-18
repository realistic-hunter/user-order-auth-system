package com.liushipin.userorderauthsystem.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类
 *
 * 作用：
 * 1. 对明文密码进行 BCrypt 加密
 * 2. 校验用户输入的明文密码和数据库中的密文是否匹配
 */
public class PasswordUtil {

    /**
     * BCrypt 密码编码器
     *
     * BCrypt 的特点：
     * 1. 每次加密同一个密码，结果都不一样
     * 2. 密文中自带盐值
     * 3. 比普通 MD5 更安全
     */
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 加密密码
     *
     * @param rawPassword 明文密码，例如 123456
     * @return BCrypt 加密后的密码
     */
    public static String encode(String rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 校验密码
     *
     * @param rawPassword 前端传来的明文密码
     * @param encodedPassword 数据库里存储的 BCrypt 密文
     * @return true 表示密码正确，false 表示密码错误
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }
}