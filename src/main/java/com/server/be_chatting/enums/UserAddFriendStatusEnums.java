package com.server.be_chatting.enums;

public enum UserAddFriendStatusEnums {
    PASS(1, "通过"),
    REJECT(2, "拒绝"),
    APPLY(3, "申请中");
    private int code;
    private String desc;

    UserAddFriendStatusEnums(int code, String desc) {
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
