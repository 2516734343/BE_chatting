package com.server.be_chatting.dto;

import lombok.Data;

@Data
public class ChatMsgDto {
    private Long userId;
    private Long targetUserId;
    private String content;
}