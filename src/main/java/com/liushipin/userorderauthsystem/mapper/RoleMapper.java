package com.liushipin.userorderauthsystem.mapper;

import com.liushipin.userorderauthsystem.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    // 根据角色 ID 查询权限列表，SQL 语句中 #{roleId} 会自动替换为方法参数 roleId 的值
    @Select("""
            SELECT p.id, p.permission_name, p.permission_code, p.description, p.create_time, p.update_time
            FROM role_permission rp
            JOIN permissions p ON rp.permission_id = p.id
            WHERE rp.role_id = #{roleId}
            """)
    List<Permission> findPermissionsByRoleId(Long roleId);
}