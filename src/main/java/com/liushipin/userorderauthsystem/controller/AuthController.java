package com.liushipin.userorderauthsystem.controller;

import com.liushipin.userorderauthsystem.common.Result;
import com.liushipin.userorderauthsystem.dto.LoginDTO;
import com.liushipin.userorderauthsystem.service.AuthService;
import com.liushipin.userorderauthsystem.vo.LoginVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 登录认证接口
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }
}
