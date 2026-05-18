package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.vo.UserVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 查询所有用户
     */
    List<UserVO> listUsers();

    /**
     * 根据 ID 查询用户
     */
    UserVO getUserById(Long id);
}