package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.dto.OrderCreateDTO;
import com.liushipin.userorderauthsystem.entity.Order;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     */
    void createOrder(OrderCreateDTO dto);

    /**
     * 查询当前登录用户自己的订单
     */
    List<Order> getMyOrders();

    /**
     * 修改订单状态
     */
    void updateOrderStatus(Long id, Integer status);

    /**
     * 查询全部订单
     */
    List<Order> listAllOrders();

    /**
     * 删除订单
     */
    void deleteOrder(Long id);
}