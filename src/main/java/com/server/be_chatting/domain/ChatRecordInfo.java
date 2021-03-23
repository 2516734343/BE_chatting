package com.server.be_chatting.domain;

import lombok.Data;

@Data
public class ChatRecordInfo {
    private Long id;
    private Long userId;
    private Long targetUserId;
    private String content;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
