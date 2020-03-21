package com.zh.community.community.exception;

/**
 * created by ${host}
 */
public enum CustomizeErrorCodeEnum implements CustomizeErrorCode {
    NOT_LOGING(1000,"您未登入！！！"),
    QUESTION_NOT_FOUND(1001,"您访问的问题不存在，换个姿势试试呢！！！"),
    SYS_ERROR(500,"系统内部错误！！！");
    private Integer code;
    private String message;

    CustomizeErrorCodeEnum(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
