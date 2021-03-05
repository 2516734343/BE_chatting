package com.server.be_chatting.vo;

import lombok.Data;

@Data
public class LoginVo {
    private Integer loginStatus;
    private Long userId;
    private String username;
    private String authentication;
}
