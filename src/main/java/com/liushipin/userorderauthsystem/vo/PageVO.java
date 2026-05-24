package com.liushipin.userorderauthsystem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "分页返回对象")
public class PageVO<T> {

    @Schema(description = "总记录数", example = "100")
    private Long total;

    @Schema(description = "当前页码", example = "1")
    private Integer pageNum;

    @Schema(description = "每页数量", example = "10")
    private Integer pageSize;

    @Schema(description = "总页数", example = "10")
    private Integer pages;

    @Schema(description = "当前页数据")
    private List<T> records;
}
