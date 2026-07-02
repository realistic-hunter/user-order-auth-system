package com.liushipin.userorderauthsystem.service.impl;

import com.liushipin.userorderauthsystem.dto.LoginDTO;
import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.security.JwtService;
import com.liushipin.userorderauthsystem.security.LoginUser;
import com.liushipin.userorderauthsystem.service.AuthService;
import com.liushipin.userorderauthsystem.service.LoginAttemptService;
import com.liushipin.userorderauthsystem.service.TokenBlacklistService;
import com.liushipin.userorderauthsystem.vo.LoginVO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           LoginAttemptService loginAttemptService,
                           TokenBlacklistService tokenBlacklistService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.loginAttemptService = loginAttemptService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        if (loginAttemptService.isBlocked(dto.getUsername())) {
            throw new BusinessException(429, "登录失败次数过多，请稍后再试");
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (AuthenticationException e) {
            loginAttemptService.recordFailure(dto.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }

        loginAttemptService.clear(dto.getUsername());

        LoginUser user = (LoginUser) authentication.getPrincipal();
        String token = jwtService.generateToken(user.getUserId(), user.getUsername());

        LoginVO vo = new LoginVO();
        vo.setUserId(user.getUserId());
        vo.setUsername(user.getUsername());
        vo.setToken(token);
        return vo;
    }

    @Override
    public void logout(String token) {
        tokenBlacklistService.blacklist(token);
    }
}
