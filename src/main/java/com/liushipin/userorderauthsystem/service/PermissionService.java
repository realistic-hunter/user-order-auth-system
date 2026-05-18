package com.liushipin.userorderauthsystem.service;

/**
 * 权限服务接口
 */
public interface PermissionService {

    /**
     * 校验用户是否拥有指定权限
     *
     * @param userId 用户 ID
     * @param permissionCode 权限码，例如 order:update
     * @param errorMessage 没有权限时的提示信息
     */
    void checkPermission(Long userId, String permissionCode, String errorMessage);
}