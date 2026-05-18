package com.liushipin.userorderauthsystem.mapper;

import com.liushipin.userorderauthsystem.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("""
            SELECT p.id, p.permission_name, p.permission_code, p.description, p.create_time, p.update_time
            FROM role_permission rp
            JOIN permissions p ON rp.permission_id = p.id
            WHERE rp.role_id = #{roleId}
            """)
    List<Permission> findPermissionsByRoleId(Long roleId);
}