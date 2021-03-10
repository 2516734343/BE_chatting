package com.server.be_chatting.domain;

import lombok.Data;

@Data
public class CommentInfo {
    private Long id;
    private Long invitationId;
    private String content;
    private Long userId;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
