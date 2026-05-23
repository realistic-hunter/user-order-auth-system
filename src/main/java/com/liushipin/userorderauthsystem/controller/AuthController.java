package com.liushipin.userorderauthsystem.controller;

import com.liushipin.userorderauthsystem.common.Result;
import com.liushipin.userorderauthsystem.dto.LoginDTO;
import com.liushipin.userorderauthsystem.service.AuthService;
import com.liushipin.userorderauthsystem.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 登录认证接口
 */

@Tag(name = "认证管理", description = "用户登录、退出登录等接口")
@RestController
@RequestMapping("/auth")
public class AuthController {
    //AuthController 依赖 AuthService 来处理登录逻辑
    private final AuthService authService;
    // 构造方法注入 AuthService，Spring 会自动把 AuthServiceImpl 注入进来
    public AuthController(AuthService authService) {this.authService = authService;}

    /**
     * 登录接口
     */
    @Operation(summary = "用户登录", description = "用户使用用户名和密码登录，成功后返回 JWT token")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }
}
