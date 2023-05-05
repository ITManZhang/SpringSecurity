package com.paidaxing.security.common;

public enum ResultCode {
    SUCCESS(200,"操作成功"),
    FAIL(500,"操作失败"),
    ERROR(502,"未知错误");
    private Integer code;
    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
