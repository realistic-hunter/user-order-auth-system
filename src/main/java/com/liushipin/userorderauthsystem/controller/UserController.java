package com.liushipin.userorderauthsystem.controller;

import com.liushipin.userorderauthsystem.common.Result;
import com.liushipin.userorderauthsystem.service.UserService;
import com.liushipin.userorderauthsystem.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户接口控制器
 */
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/users")
    public Result<List<UserVO>> listUsers() {
        return Result.success(userService.listUsers());
    }

    /**
     * 根据 ID 查询用户
     */
    @GetMapping("/users/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }
}