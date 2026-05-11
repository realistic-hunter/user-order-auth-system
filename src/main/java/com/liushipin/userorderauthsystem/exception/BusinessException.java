package com.liushipin.userorderauthsystem.exception;

public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(String message) {//构造方法
        super(message);//父类构造方法
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;//
    }

    public Integer getCode() {//获取code
        return code;
    }
}