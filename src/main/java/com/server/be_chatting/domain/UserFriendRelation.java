package com.server.be_chatting.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class UserFriendRelation {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long targetUserId;
    private Integer status;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
