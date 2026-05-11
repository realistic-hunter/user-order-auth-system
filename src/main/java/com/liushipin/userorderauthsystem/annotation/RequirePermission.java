package com.liushipin.userorderauthsystem.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 *
 * 作用：
 * 在需要权限控制的方法上添加该注解，
 * AOP 会自动读取注解中的权限码，并进行权限校验。
 *
 * 示例：
 * @RequirePermission("order:update")
 */
@Target(ElementType.METHOD) // 表示这个注解只能加在方法上
@Retention(RetentionPolicy.RUNTIME) // 表示运行时仍然保留，AOP 才能读取到
@Documented
public @interface RequirePermission {

    /**
     * 权限码
     *
     * 示例：
     * order:update
     * order:list
     * order:delete
     */
    String value();

    /**
     * 没有权限时返回的错误信息
     *
     * 如果不写，就默认返回“没有操作权限”
     */
    String message() default "没有操作权限";
}
