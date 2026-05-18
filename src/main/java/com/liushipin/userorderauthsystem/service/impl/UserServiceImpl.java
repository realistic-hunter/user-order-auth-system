package com.liushipin.userorderauthsystem.service.impl;

import com.liushipin.userorderauthsystem.entity.User;
import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.mapper.UserMapper;
import com.liushipin.userorderauthsystem.service.UserService;
import com.liushipin.userorderauthsystem.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 查询所有用户，并转换成 UserVO
     */
    @Override
    public List<UserVO> listUsers() {
        return userMapper.findAll()
                .stream()
                .map(this::convertToUserVO)
                .collect(Collectors.toList());
    }

    /**
     * 根据 ID 查询用户，并转换成 UserVO
     */
    @Override
    public UserVO getUserById(Long id) {
        User user = userMapper.findById(id);

        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        return convertToUserVO(user);
    }

    /**
     * Entity 转 VO
     *
     * 注意：
     * 这里故意不设置 password，避免把密码返回给前端
     */
    private UserVO convertToUserVO(User user) {
        UserVO vo = new UserVO();

        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setCreateTime(user.getCreateTime());
        vo.setUpdateTime(user.getUpdateTime());

        return vo;
    }
}