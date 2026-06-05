package com.liushipin.userorderauthsystem.service.impl;

import com.liushipin.userorderauthsystem.dto.LoginDTO;
import com.liushipin.userorderauthsystem.entity.User;
import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.mapper.UserMapper;
import com.liushipin.userorderauthsystem.service.AuthService;
import com.liushipin.userorderauthsystem.service.LoginAttemptService;
import com.liushipin.userorderauthsystem.service.TokenBlacklistService;
import com.liushipin.userorderauthsystem.util.JwtUtil;
import com.liushipin.userorderauthsystem.util.PasswordUtil;
import com.liushipin.userorderauthsystem.vo.LoginVO;
import org.springframework.stereotype.Service;

/**
 * 登录认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final LoginAttemptService loginAttemptService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthServiceImpl(UserMapper userMapper,
                           LoginAttemptService loginAttemptService,
                           TokenBlacklistService tokenBlacklistService) {
        this.userMapper = userMapper;
        this.loginAttemptService = loginAttemptService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        if (loginAttemptService.isBlocked(dto.getUsername())) {
            throw new BusinessException(429, "登录失败次数过多，请稍后再试");
        }

        // 1. 根据用户名查询用户
        User user = userMapper.findByUsername(dto.getUsername());

        // 用户不存在时，不明确提示“用户不存在”，避免暴露账号信息
        if (user == null) {
            loginAttemptService.recordFailure(dto.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 2. 使用 BCrypt 校验密码
        // dto.getPassword() 是前端传来的明文密码
        // user.getPassword() 是数据库中保存的 BCrypt 密文
        if (!PasswordUtil.matches(dto.getPassword(), user.getPassword())) {
            loginAttemptService.recordFailure(dto.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }

        loginAttemptService.clear(dto.getUsername());

        // 3. 密码正确，生成 JWT token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());

        // 4. 封装登录返回结果
        LoginVO vo = new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setToken(token);

        return vo;
    }

    @Override
    public void logout(String token) {
        tokenBlacklistService.blacklist(token);
    }
}
