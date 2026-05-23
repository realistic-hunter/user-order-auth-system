package com.liushipin.userorderauthsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建订单请求参数
 */
@Data
@Schema(name = "OrderCreateDTO", description = "创建订单请求参数")
public class OrderCreateDTO {

    @Schema(description = "订单金额", example = "99.99", required = true)
    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.01", message = "订单金额必须大于0")
    private BigDecimal amount;

    @Schema(description = "订单备注", example = "这是一个测试订单")
    private String remark;
}