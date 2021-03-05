package com.server.be_chatting.enums;


public enum LoginStatusEnums {
    SUCCESS(0, "登录成功"),
    FAIL(1, "登录失败");
    private int code;
    private String desc;

    LoginStatusEnums(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
