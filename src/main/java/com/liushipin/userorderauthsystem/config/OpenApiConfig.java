package com.liushipin.userorderauthsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 配置类
 *
 * 作用：
 * 1. 配置接口文档基础信息
 * 2. 配置 JWT Bearer Token 认证
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userOrderAuthOpenAPI() {
        // 安全方案名称，下面 SecurityRequirement 和 Components 要保持一致
        String securitySchemeName = "BearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("用户订单权限系统 API 文档")
                        .version("v1.0")
                        .description("基于 Spring Boot + MyBatis + JWT + RBAC 的用户订单权限管理系统"))
                // 给接口文档添加全局认证要求
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 配置 JWT Bearer Token
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
