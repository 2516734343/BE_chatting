package com.server.be_chatting.domain;

import lombok.Data;

@Data
public class UserFriendRelation {
    private Long id;
    private Long userId;
    private Long targetUserId;
    private Integer status;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
