package com.liushipin.userorderauthsystem.vo;

import lombok.Data;

/**
 * 登录成功后返回给前端的数据
 */
@Data
public class LoginVO {

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * JWT token
     */
    private String token;
}
