package com.liushipin.userorderauthsystem.aspect;

import com.liushipin.userorderauthsystem.annotation.RequirePermission;
import com.liushipin.userorderauthsystem.common.UserContext;
import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.service.PermissionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 权限校验切面
 *
 * 作用：
 * 1. 拦截所有加了 @RequirePermission 的方法
 * 2. 读取注解中的权限码
 * 3. 获取当前登录用户 ID
 * 4. 调用 PermissionService 进行权限校验
 */
@Aspect
@Component
public class PermissionAspect {

    private final PermissionService permissionService;

    public PermissionAspect(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 在目标方法执行前进行权限校验
     *
     * @param joinPoint 被拦截的方法信息，暂时不用也可以保留
     * @param requirePermission 方法上的权限注解
     */
    @Before("@annotation(requirePermission)")
    public void checkPermission(JoinPoint joinPoint, RequirePermission requirePermission) {
        // 从注解中读取权限码，例如 order:update
        String permissionCode = requirePermission.value();

        // 从注解中读取无权限提示信息
        String errorMessage = requirePermission.message();

        // 从 UserContext 中获取当前登录用户 ID
        // 这个 userId 是 AuthInterceptor 从 token 中解析出来后放进去的
        Long userId = UserContext.getUserId();

        // 理论上只要经过 AuthInterceptor，这里就应该有 userId
        // 如果没有，说明请求没有正常经过登录认证流程
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }

        // 调用权限服务进行校验
        permissionService.checkPermission(userId, permissionCode, errorMessage);
    }
}