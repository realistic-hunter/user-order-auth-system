package com.liushipin.userorderauthsystem.controller;

import com.liushipin.userorderauthsystem.common.Result;
import com.liushipin.userorderauthsystem.dto.OrderCreateDTO;
import com.liushipin.userorderauthsystem.dto.OrderStatusUpdateDTO;
import com.liushipin.userorderauthsystem.entity.Order;
import com.liushipin.userorderauthsystem.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单接口控制器
 */
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建订单
     *
     * 当前用户 ID 从 token 中获取，不再由前端传 userId
     */
    @PostMapping("/orders")
    public Result<String> createOrder(@Valid @RequestBody OrderCreateDTO dto) {
        orderService.createOrder(dto);
        return Result.success("订单创建成功");
    }

    /**
     * 查询当前登录用户自己的订单
     */
    @GetMapping("/orders/my")
    public Result<List<Order>> getMyOrders() {
        return Result.success(orderService.getMyOrders());
    }

    /**
     * 修改订单状态
     *
     * 管理员权限：order:update
     */
    @PutMapping("/orders/{id}/status")
    public Result<String> updateOrderStatus(@PathVariable Long id,
                                            @Valid @RequestBody OrderStatusUpdateDTO dto) {
        orderService.updateOrderStatus(id, dto.getStatus());
        return Result.success("订单状态修改成功");
    }

    /**
     * 查询全部订单
     *
     * 管理员权限：order:list
     */
    @GetMapping("/orders")
    public Result<List<Order>> listAllOrders() {
        return Result.success(orderService.listAllOrders());
    }

    /**
     * 删除订单
     *
     * 管理员权限：order:delete
     */
    @DeleteMapping("/orders/{id}")
    public Result<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return Result.success("订单删除成功");
    }
}