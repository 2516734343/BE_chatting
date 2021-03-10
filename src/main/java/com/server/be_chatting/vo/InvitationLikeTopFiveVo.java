package com.server.be_chatting.vo;

import lombok.Data;

@Data
public class InvitationLikeTopFiveVo {
    private Long id;
    private String content;
    private Integer likeNum;
}
