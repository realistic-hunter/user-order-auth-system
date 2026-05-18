package com.liushipin.userorderauthsystem.controller;

import com.liushipin.userorderauthsystem.entity.Permission;
import com.liushipin.userorderauthsystem.mapper.RoleMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleMapper roleMapper;

    public RoleController(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @GetMapping("/{roleId}/permissions")
    public List<Permission> getPermissionsByRoleId(@PathVariable Long roleId) {
        return roleMapper.findPermissionsByRoleId(roleId);
    }
}