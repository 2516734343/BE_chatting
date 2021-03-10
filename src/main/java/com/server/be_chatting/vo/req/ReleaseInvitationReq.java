package com.server.be_chatting.vo.req;

import lombok.Data;

@Data
public class ReleaseInvitationReq {
    private Long userId;
    private String content;
}
