package com.server.be_chatting.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class InvitationInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String content;
    private Long userId;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
