package com.server.be_chatting.vo.req;

import lombok.Data;

@Data
public class AddUserFriendReq {
    private Long userId;
    private Long targetUserId;
    private String content;
}
