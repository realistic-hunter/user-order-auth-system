package com.liushipin.userorderauthsystem.service.impl;

import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.mapper.PermissionMapper;
import com.liushipin.userorderauthsystem.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限服务实现类
 *
 * 专门负责权限判断。
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Override
    public void checkPermission(Long userId, String permissionCode, String errorMessage) {
        // 查询当前用户拥有的所有权限码
        List<String> permissionCodes = permissionMapper.findPermissionCodesByUserId(userId);

        // 没有权限列表，或者权限列表中不包含目标权限码，就抛出 403
        if (permissionCodes == null || !permissionCodes.contains(permissionCode)) {
            throw new BusinessException(403, errorMessage);
        }
    }
}