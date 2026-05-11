package com.liushipin.userorderauthsystem.mapper;

import com.liushipin.userorderauthsystem.entity.Role;
import com.liushipin.userorderauthsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 查询所有用户
     */
    @Select("""
            select id,
                   username,
                   password,
                   nickname,
                   create_time as createTime,
                   update_time as updateTime
            from users
            """)
    List<User> findAll();

    /**
     * 根据 id 查询用户
     */
    @Select("""
            select id,
                   username,
                   password,
                   nickname,
                   create_time as createTime,
                   update_time as updateTime
            from users
            where id = #{id}
            """)
    User findById(Long id);

    /**
     * 根据用户 id 查询角色
     */
    @Select("""
            SELECT r.id, r.role_name, r.role_code, r.description, r.create_time, r.update_time
            FROM user_role ur
            JOIN roles r ON ur.role_id = r.id
            WHERE ur.user_id = #{userId}
            """)
    List<Role> findRolesByUserId(Long userId);

    /**
     * 根据用户名查询用户
     */
    @Select("""
        select id,
               username,
               password,
               nickname,
               create_time as createTime,
               update_time as updateTime
        from users
        where username = #{username}
        """)
    User findByUsername(String username);

}