package com.liushipin.userorderauthsystem.mapper;

import com.liushipin.userorderauthsystem.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("""
            insert into orders(order_no, user_id, amount, status, remark)
            values(#{orderNo}, #{userId}, #{amount}, #{status}, #{remark})
            """)
    void insert(Order order);

    @Select("""
            select id,
                   order_no as orderNo,
                   user_id as userId,
                   amount,
                   status,
                   remark,
                   create_time as createTime,
                   update_time as updateTime
            from orders
            where user_id = #{userId}
            order by id desc
            """)
    List<Order> findByUserId(Long userId);

    @Select("""
            select id,
                   order_no as orderNo,
                   user_id as userId,
                   amount,
                   status,
                   remark,
                   create_time as createTime,
                   update_time as updateTime
            from orders
            where id = #{id}
            """)
    Order findById(Long id);

    @Update("""
            update orders
            set status = #{status}
            where id = #{id}
            """)
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("""
            select id,
                   order_no as orderNo,
                   user_id as userId,
                   amount,
                   status,
                   remark,
                   create_time as createTime,
                   update_time as updateTime
            from orders
            order by id desc
            """)
    List<Order> findAll();

    @Delete("""
            delete from orders
            where id = #{id}
            """)
    void deleteById(Long id);
}