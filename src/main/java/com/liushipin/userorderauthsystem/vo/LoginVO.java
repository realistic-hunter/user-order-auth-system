package com.liushipin.userorderauthsystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录成功后返回给前端的数据
 */
@Data
@Schema(description = "登录成功后返回给前端的数据")
public class LoginVO {

    @Schema(description = "用户 ID", example = "123")
    private Long userId;

    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @Schema(description = "JWT 令牌", example = "")
    private String token;
}
