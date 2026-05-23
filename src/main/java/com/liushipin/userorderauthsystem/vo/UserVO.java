package com.liushipin.userorderauthsystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户返回对象
 *
 * VO 的作用：
 * 1. 控制接口返回给前端的数据
 * 2. 避免把 password 等敏感字段返回出去
 */
@Schema(description = "用户返回对象")
@Data
public class UserVO {

    @Schema(description = "用户 ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    @Schema(description = "创建时间",example = "2024-06-01T12:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2024-06-01T12:00:00")
    private LocalDateTime updateTime;
}