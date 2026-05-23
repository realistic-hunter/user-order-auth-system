package com.liushipin.userorderauthsystem.controller;

import com.liushipin.userorderauthsystem.common.Result;
import com.liushipin.userorderauthsystem.service.UserService;
import com.liushipin.userorderauthsystem.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户接口控制器
 */
@RestController
@Tag(name = "用户管理", description = "查询用户信息等接口")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "查询所有用户", description = "管理员查询系统中所有用户的信息列表")
    @GetMapping("/users")
    public Result<List<UserVO>> listUsers() {
        return Result.success(userService.listUsers());
    }

    @Operation(summary = "根据 ID 查询用户", description = "管理员根据用户 ID 查询该用户的详细信息")
    @GetMapping("/users/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }
}