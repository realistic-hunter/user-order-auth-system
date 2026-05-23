package com.liushipin.userorderauthsystem.controller;

import com.liushipin.userorderauthsystem.common.Result;
import com.liushipin.userorderauthsystem.dto.OrderCreateDTO;
import com.liushipin.userorderauthsystem.dto.OrderStatusUpdateDTO;
import com.liushipin.userorderauthsystem.entity.Order;
import com.liushipin.userorderauthsystem.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单接口控制器
 */
@RestController
@Tag(name = "订单管理", description = "订单创建、查询、状态修改等接口")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 分页查询订单列表
     */
    @Operation(summary = "分页查询订单列表", description = "管理员分页查看系统中的订单列表")
    @GetMapping("/orders")
    public Result<List<Order>> listAllOrders() {
        return Result.success(orderService.listAllOrders());
    }

    /**
     * 创建订单
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
     * 管理员权限：order:update
     */
    @PutMapping("/orders/{id}/status")
    public Result<String> updateOrderStatus(@PathVariable Long id,
                                            @Valid @RequestBody OrderStatusUpdateDTO dto) {
        orderService.updateOrderStatus(id, dto.getStatus());
        return Result.success("订单状态修改成功");
    }


    /**
     * 删除订单
     * 管理员权限：order:delete
     */
    @DeleteMapping("/orders/{id}")
    public Result<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return Result.success("订单删除成功");
    }
}