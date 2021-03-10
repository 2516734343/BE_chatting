package com.server.be_chatting.vo;

import lombok.Data;

@Data
public class CommentVo {
    private Long commentId;
    private String content;
    private String userName;
    private String name;
    private Long userId;
    private String photo;
    private Long time;
}
