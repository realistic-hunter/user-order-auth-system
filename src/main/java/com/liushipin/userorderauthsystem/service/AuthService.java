package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.dto.LoginDTO;
import com.liushipin.userorderauthsystem.vo.LoginVO;

/**
 * 登录认证服务
 */
public interface AuthService {

    /**
     * 用户登录
     */
    LoginVO login(LoginDTO dto);
}
