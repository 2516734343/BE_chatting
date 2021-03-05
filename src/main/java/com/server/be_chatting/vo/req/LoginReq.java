package com.server.be_chatting.vo.req;

import lombok.Data;


@Data
public class LoginReq {
    private String username;
    private String password;
}
