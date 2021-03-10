package com.server.be_chatting.vo;

import lombok.Data;

@Data
public class InvitationVo {
    private Long id;
    private String content;
    private String userName;
    private String name;
    private Long userId;
    private String photo;
    private Integer likeNum;
    private Integer commentNum;
    private Long time;
}
