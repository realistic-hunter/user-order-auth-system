package com.liushipin.userorderauthsystem.common;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;//状态码
    private String message;//提示信息
    private T data;// 数据

    public Result() {
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {//创建成功结果
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success(String message, T data) {//创建成功结果
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> fail(String message) {//创建失败结果
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> fail(Integer code, String message) {//创建失败结果
        return new Result<>(code, message, null);
    }

}