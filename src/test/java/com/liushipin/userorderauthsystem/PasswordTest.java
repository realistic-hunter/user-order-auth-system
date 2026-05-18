package com.liushipin.userorderauthsystem;

import com.liushipin.userorderauthsystem.util.PasswordUtil;

/**
 * 临时生成 BCrypt 密码的测试类
 *
 * 用途：
 * 运行 main 方法，生成 123456 对应的 BCrypt 密文。
 * 生成后复制到数据库中。
 */
public class PasswordTest {

    public static void main(String[] args) {
        // 要加密的明文密码
        String rawPassword = "123456";

        // 生成 BCrypt 密文
        String encodedPassword = PasswordUtil.encode(rawPassword);

        // 打印密文
        System.out.println(encodedPassword);
    }
}