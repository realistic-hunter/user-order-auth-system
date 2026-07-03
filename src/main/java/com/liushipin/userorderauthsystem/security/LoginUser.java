package com.liushipin.userorderauthsystem.security;

import com.liushipin.userorderauthsystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class LoginUser implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;

    public LoginUser(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
