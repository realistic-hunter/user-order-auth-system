package com.liushipin.userorderauthsystem.mapper;

import com.liushipin.userorderauthsystem.entity.Order;
import com.liushipin.userorderauthsystem.vo.OrderVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    //插入订单数据，order 对象中的属性会自动映射到 SQL 语句中的 #{属性名}
    @Insert("""
            insert into orders(order_no, user_id, amount, status, remark)
            values(#{orderNo}, #{userId}, #{amount}, #{status}, #{remark})
            """)
    void insert(Order order);

    //根据用户 ID 查询订单列表，返回 List<Order>，SQL 语句中的 #{userId} 会自动替换为方法参数 userId 的值
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

    //根据订单 ID 查询订单详情，返回 Order 对象，SQL 语句中的 #{id} 会自动替换为方法参数 id 的值
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

    //根据订单 ID 修改订单状态，SQL 语句中的 #{id} 和 #{status} 会自动替换为方法参数 id 和 status 的值
    @Update("""
            update orders
            set status = #{status}
            where id = #{id}
            """)
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    //查询全部订单列表，返回 List<Order>，SQL 语句中没有参数，所以不需要 #{参数名}
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

    /*
     * 分页查询订单列表。
     *
     * status 为 null 时查询全部订单；
     * status 不为 null 时追加 where status = #{status} 条件。
     * limit + offset 是手写分页的核心语法。
     */
    @Select("""
            <script>
            select id,
                   order_no as orderNo,
                   user_id as userId,
                   amount,
                   status,
                   remark,
                   create_time as createTime,
                   update_time as updateTime
            from orders
            <where>
                <if test="status != null">
                    status = #{status}
                </if>
            </where>
            order by id desc
            limit #{pageSize} offset #{offset}
            </script>
            """)
    List<OrderVO> findPageByStatus(@Param("status") Integer status,
                                   @Param("offset") Long offset,
                                   @Param("pageSize") Integer pageSize);

    /*
     * 查询符合筛选条件的订单总数。
     *
     * total 用于计算总页数 pages，也会返回给前端展示分页信息。
     */
    @Select("""
            <script>
            select count(*)
            from orders
            <where>
                <if test="status != null">
                    status = #{status}
                </if>
            </where>
            </script>
            """)
    Long countByStatus(@Param("status") Integer status);

    //根据订单 ID 删除订单
    @Delete("""
            delete from orders
            where id = #{id}
            """)
    void deleteById(Long id);
}
