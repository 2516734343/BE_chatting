package com.server.be_chatting.vo;

import lombok.Data;


@Data
public class ChatRecordVo {
    private Long recordId;
    private Long userId;
    private Long targetUserId;
    private String content;
}
