package com.liushipin.userorderauthsystem.service.impl;

import com.liushipin.userorderauthsystem.dto.LoginDTO;
import com.liushipin.userorderauthsystem.entity.User;
import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.mapper.UserMapper;
import com.liushipin.userorderauthsystem.service.AuthService;
import com.liushipin.userorderauthsystem.util.JwtUtil;
import com.liushipin.userorderauthsystem.vo.LoginVO;
import org.springframework.stereotype.Service;

/**
 * 登录认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    public AuthServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        // 根据用户名查询用户
        User user = userMapper.findByUsername(dto.getUsername());

        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        /*
         * 当前是学习项目，先用明文密码比较。
         * 后续正式项目应该使用 BCrypt 加密密码。
         */
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 生成 token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());

        // 封装返回结果
        LoginVO vo = new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setToken(token);

        return vo;
    }
}
