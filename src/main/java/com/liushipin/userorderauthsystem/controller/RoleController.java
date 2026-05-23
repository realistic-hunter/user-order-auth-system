package com.liushipin.userorderauthsystem.controller;

import com.liushipin.userorderauthsystem.entity.Permission;
import com.liushipin.userorderauthsystem.mapper.RoleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色管理", description = "查询角色权限等接口")
@RestController
@RequestMapping("/roles")
public class RoleController {
    //RoleController 依赖 RoleMapper 来查询角色权限数据
    private final RoleMapper roleMapper;
    public RoleController(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Operation(summary = "根据角色 ID 查询权限列表", description = "管理员根据角色 ID 查询该角色拥有的权限列表")
    @GetMapping("/{roleId}/permissions")
    public List<Permission> getPermissionsByRoleId(@PathVariable Long roleId) {
        return roleMapper.findPermissionsByRoleId(roleId);
    }
}