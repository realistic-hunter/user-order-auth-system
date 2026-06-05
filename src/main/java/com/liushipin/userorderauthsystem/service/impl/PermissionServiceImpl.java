package com.liushipin.userorderauthsystem.service.impl;

import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.mapper.PermissionMapper;
import com.liushipin.userorderauthsystem.service.PermissionCacheService;
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
    private final PermissionCacheService permissionCacheService;

    public PermissionServiceImpl(PermissionMapper permissionMapper,
                                 PermissionCacheService permissionCacheService) {
        this.permissionMapper = permissionMapper;
        this.permissionCacheService = permissionCacheService;
    }

    @Override
    public void checkPermission(Long userId, String permissionCode, String errorMessage) {
        List<String> permissionCodes = permissionCacheService.get(userId)
                .orElseGet(() -> loadAndCachePermissions(userId));

        // 没有权限列表，或者权限列表中不包含目标权限码，就抛出 403
        if (permissionCodes == null || !permissionCodes.contains(permissionCode)) {
            throw new BusinessException(403, errorMessage);
        }
    }

    private List<String> loadAndCachePermissions(Long userId) {
        List<String> permissionCodes = permissionMapper.findPermissionCodesByUserId(userId);
        List<String> safePermissionCodes = permissionCodes == null ? List.of() : permissionCodes;
        permissionCacheService.put(userId, safePermissionCodes);
        return safePermissionCodes;
    }
}
