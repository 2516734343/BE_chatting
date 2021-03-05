package com.server.be_chatting.enums;


public enum UserTypeEnums {
    COMMON_USER(1, "普通用户"),
    ADMIN(0, "管理员");
    private int code;
    private String desc;

    UserTypeEnums(int code, String desc) {
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
