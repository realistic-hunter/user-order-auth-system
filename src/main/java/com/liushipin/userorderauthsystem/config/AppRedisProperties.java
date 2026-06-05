package com.liushipin.userorderauthsystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "app.redis")
public class AppRedisProperties {

    private Duration permissionCacheTtl = Duration.ofMinutes(30);
    private Duration loginFailureWindow = Duration.ofMinutes(15);
    private int maxLoginFailures = 5;

    public Duration getPermissionCacheTtl() {
        return permissionCacheTtl;
    }

    public void setPermissionCacheTtl(Duration permissionCacheTtl) {
        this.permissionCacheTtl = permissionCacheTtl;
    }

    public Duration getLoginFailureWindow() {
        return loginFailureWindow;
    }

    public void setLoginFailureWindow(Duration loginFailureWindow) {
        this.loginFailureWindow = loginFailureWindow;
    }

    public int getMaxLoginFailures() {
        return maxLoginFailures;
    }

    public void setMaxLoginFailures(int maxLoginFailures) {
        this.maxLoginFailures = maxLoginFailures;
    }
}
