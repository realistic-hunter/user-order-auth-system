package com.liushipin.userorderauthsystem.interceptor;

import com.liushipin.userorderauthsystem.common.UserContext;
import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录认证拦截器
 *
 * 作用：
 * 1. 拦截需要登录的接口
 * 2. 从请求头 Authorization 中获取 token
 * 3. 校验 token
 * 4. 解析 userId，放入 UserContext
 */
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * Controller 执行前触发
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // 从请求头获取 Authorization
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(401, "请先登录");
        }

        // 去掉 Bearer 前缀，得到真正的 token
        String token = authorization.substring(7);

        try {
            // 校验 token 是否有效
            JwtUtil.validateToken(token);

            // 从 token 中解析 userId
            Long userId = JwtUtil.getUserId(token);

            // 保存当前登录用户 ID
            UserContext.setUserId(userId);

            return true;
        } catch (Exception e) {
            throw new BusinessException(401, "登录状态无效，请重新登录");
        }
    }

    /**
     * 请求结束后触发
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        // 清理 ThreadLocal，避免内存泄漏
        UserContext.clear();
    }
}