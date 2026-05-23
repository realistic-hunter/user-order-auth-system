package com.liushipin.userorderauthsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改订单状态请求参数
 */
@Data
public class OrderStatusUpdateDTO {

    @Schema(description = "订单状态：0=待支付，1=已支付，2=已发货，" +
            "3=已完成，4=已取消", example = "1")
    @NotNull(message = "订单状态不能为空")
    @Min(value = 0, message = "订单状态不能小于0")
    @Max(value = 4, message = "订单状态不能大于4")
    private Integer status;
}