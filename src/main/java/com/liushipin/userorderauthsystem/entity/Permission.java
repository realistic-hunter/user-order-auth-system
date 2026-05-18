package com.liushipin.userorderauthsystem.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Permission {
    private Long id;
    private String permissionName;
    private String permissionCode;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}