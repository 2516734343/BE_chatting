package com.server.be_chatting.vo.req;

import lombok.Data;

@Data
public class InvitationCommentReq {
    private Long invitationId;
    private Long userId;
    private String content;
}
