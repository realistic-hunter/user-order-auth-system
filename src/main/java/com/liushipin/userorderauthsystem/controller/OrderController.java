package com.liushipin.userorderauthsystem.controller;

import com.liushipin.userorderauthsystem.common.Result;
import com.liushipin.userorderauthsystem.dto.OrderCreateDTO;
import com.liushipin.userorderauthsystem.dto.OrderStatusUpdateDTO;
import com.liushipin.userorderauthsystem.entity.Order;
import com.liushipin.userorderauthsystem.service.OrderService;
import com.liushipin.userorderauthsystem.vo.OrderVO;
import com.liushipin.userorderauthsystem.vo.PageVO;
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

    @Operation(summary = "分页查询订单列表", description = "管理员分页查看系统中的订单列表")
    @GetMapping("/orders")
    public Result<List<Order>> listAllOrders() {
        return Result.success(orderService.listAllOrders());
    }

    /**
     * 分页查询订单列表。
     *
     * Controller 只负责接收请求参数并调用 Service，
     * 分页计算和业务规则放在 Service 层处理。
     */
    @Operation(summary = "分页查询订单列表", description = "管理员分页查询订单列表，可按订单状态筛选")
    @GetMapping("/orders/page")
    public Result<PageVO<OrderVO>> pageOrders(@RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              @RequestParam(required = false) Integer status) {
        return Result.success(orderService.pageOrders(pageNum, pageSize, status));
    }

    @Operation(summary = "创建订单", description = "用户创建订单，订单信息由前端传入，当前登录用户 ID 从 token 中获取")
    @PostMapping("/orders")
    public Result<String> createOrder(@Valid @RequestBody OrderCreateDTO dto) {
        orderService.createOrder(dto);
        return Result.success("订单创建成功");
    }

    @Operation(summary = "查询我的订单", description = "用户查询自己创建的订单列表")
    @GetMapping("/orders/my")
    public Result<List<Order>> getMyOrders() {
        return Result.success(orderService.getMyOrders());
    }

    @Operation(summary = "修改订单状态", description = "管理员修改订单状态，订单 ID 从路径参数获取，新的订单状态由前端传入")
    @PutMapping("/orders/{id}/status")
    public Result<String> updateOrderStatus(@PathVariable Long id,
                                            @Valid @RequestBody OrderStatusUpdateDTO dto) {
        orderService.updateOrderStatus(id, dto.getStatus());
        return Result.success("订单状态修改成功");
    }

    @Operation(summary = "删除订单", description = "管理员删除订单，订单 ID 从路径参数获取")
    @DeleteMapping("/orders/{id}")
    public Result<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return Result.success("订单删除成功");
    }
}
