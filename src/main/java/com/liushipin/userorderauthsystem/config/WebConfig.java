package com.liushipin.userorderauthsystem.config;

import com.liushipin.userorderauthsystem.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Web 配置类
 *
 * 作用：
 * 注册登录认证拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                // 拦截所有接口
                .addPathPatterns("/**")
                // 放行登录接口
                .excludePathPatterns("/auth/login");
    }
}
