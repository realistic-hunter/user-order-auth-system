package com.liushipin.userorderauthsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户返回对象
 *
 * VO 的作用：
 * 1. 控制接口返回给前端的数据
 * 2. 避免把 password 等敏感字段返回出去
 */
@Data
public class UserVO {

    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}