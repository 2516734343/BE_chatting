package com.server.be_chatting.domain;

import lombok.Data;

@Data
public class InvitationInfo {
    private Long id;
    private String content;
    private Long userId;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
