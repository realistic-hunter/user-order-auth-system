package com.liushipin.userorderauthsystem.controller;

import com.liushipin.userorderauthsystem.entity.Permission;
import com.liushipin.userorderauthsystem.mapper.RoleMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    //RoleController 依赖 RoleMapper 来查询角色权限数据
    private final RoleMapper roleMapper;
    public RoleController(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    //根据角色 ID 查询权限列表
    @GetMapping("/{roleId}/permissions")
    public List<Permission> getPermissionsByRoleId(@PathVariable Long roleId) {
        return roleMapper.findPermissionsByRoleId(roleId);
    }
}