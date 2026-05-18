package com.liushipin.userorderauthsystem.service.impl;

import com.liushipin.userorderauthsystem.annotation.RequirePermission;
import com.liushipin.userorderauthsystem.common.UserContext;
import com.liushipin.userorderauthsystem.dto.OrderCreateDTO;
import com.liushipin.userorderauthsystem.entity.Order;
import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.mapper.OrderMapper;
import com.liushipin.userorderauthsystem.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单业务实现类
 *
 * 这一版已经接入 AOP 权限控制：
 * 1. 需要权限的方法，直接加 @RequirePermission
 * 2. 不再在方法内部手写 permissionService.checkPermission(...)
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 创建订单
     *
     * 当前版本：
     * 普通登录用户即可创建订单，所以暂时不加权限注解。
     */
    @Override
    public void createOrder(OrderCreateDTO dto) {
        // 从 token 中获取当前登录用户 ID
        Long userId = UserContext.getUserId();

        // 构建订单对象
        Order order = new Order();
        order.setOrderNo(generateOrderNo());

        // 不再信任前端传 userId，而是使用 token 中的 userId
        order.setUserId(userId);

        order.setAmount(dto.getAmount());
        order.setRemark(dto.getRemark());

        // 新订单默认状态：0
        order.setStatus(0);

        // 保存订单
        orderMapper.insert(order);
    }

    /**
     * 查询当前登录用户自己的订单
     *
     * 当前版本：
     * 登录用户查询自己的订单，不需要额外权限码。
     */
    @Override
    public List<Order> getMyOrders() {
        // 从 token 中获取当前登录用户 ID
        Long userId = UserContext.getUserId();

        // 查询当前用户自己的订单
        return orderMapper.findByUserId(userId);
    }

    /**
     * 修改订单状态
     *
     * 需要权限：order:update
     * AOP 会在方法执行前自动校验权限。
     */
    @Override
    @RequirePermission(value = "order:update", message = "没有权限修改订单状态")
    public void updateOrderStatus(Long id, Integer status) {
        // 判断订单是否存在
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 执行状态修改
        orderMapper.updateStatus(id, status);
    }

    /**
     * 查询全部订单
     *
     * 需要权限：order:list
     * AOP 会在方法执行前自动校验权限。
     */
    @Override
    @RequirePermission(value = "order:list", message = "没有权限查看全部订单")
    public List<Order> listAllOrders() {
        return orderMapper.findAll();
    }

    /**
     * 删除订单
     *
     * 需要权限：order:delete
     * AOP 会在方法执行前自动校验权限。
     */
    @Override
    @RequirePermission(value = "order:delete", message = "没有权限删除订单")
    public void deleteOrder(Long id) {
        // 判断订单是否存在
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 执行删除
        orderMapper.deleteById(id);
    }

    /**
     * 生成订单号
     *
     * 当前是学习项目的简单版本：
     * ORD + 当前时间戳
     */
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis();
    }
}