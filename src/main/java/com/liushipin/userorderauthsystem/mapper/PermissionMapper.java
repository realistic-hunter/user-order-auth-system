package com.liushipin.userorderauthsystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限数据访问层
 *
 * 作用：
 * 根据用户 ID，联表查询该用户拥有的所有权限码
 */
@Mapper
public interface PermissionMapper {

    /**
     * 查询指定用户拥有的所有权限码
     *
     * 关联关系：
     * user -> user_role -> role_permission -> permissions
     *
     * @param userId 用户 ID
     * @return 权限码列表，例如 order:update、order:list
     */
    @Select("""
            select distinct p.permission_code
            from user_role ur
            join role_permission rp on ur.role_id = rp.role_id
            join permissions p on rp.permission_id = p.id
            where ur.user_id = #{userId}
            """)
    List<String> findPermissionCodesByUserId(Long userId);
}