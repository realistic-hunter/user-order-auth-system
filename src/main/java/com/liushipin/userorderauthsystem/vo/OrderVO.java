package com.liushipin.userorderauthsystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单返回对象。
 *
 * VO 用于定义接口返回给前端的数据结构，避免 Controller 直接暴露数据库实体。
 */
@Data
@Schema(description = "订单返回对象")
public class OrderVO {

    @Schema(description = "订单 ID", example = "1")
    private Long id;

    @Schema(description = "订单编号", example = "ORD1717214400000")
    private String orderNo;

    @Schema(description = "用户 ID", example = "1001")
    private Long userId;

    @Schema(description = "订单金额", example = "99.99")
    private BigDecimal amount;

    @Schema(description = "订单状态：0=待支付，1=已支付，2=已发货，3=已完成，4=已取消", example = "1")
    private Integer status;

    @Schema(description = "订单备注", example = "测试订单")
    private String remark;

    @Schema(description = "创建时间", example = "2024-06-01T12:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2024-06-01T12:00:00")
    private LocalDateTime updateTime;
}
