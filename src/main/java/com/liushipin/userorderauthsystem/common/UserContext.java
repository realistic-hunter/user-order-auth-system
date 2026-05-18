package com.liushipin.userorderauthsystem.common;

/**
 * 当前登录用户上下文
 *
 * 作用：
 * 1. 拦截器解析 token 后，把 userId 放进这里
 * 2. Controller / Service 可以从这里获取当前登录用户 ID
 *
 * ThreadLocal 的意思：
 * 每个请求线程都有自己独立的一份 userId，不会互相干扰。
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();

    /**
     * 保存当前请求的用户 ID
     */
    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    /**
     * 获取当前请求的用户 ID
     */
    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    /**
     * 请求结束后清理，避免内存泄漏
     */
    public static void clear() {
        USER_ID_HOLDER.remove();
    }
}
