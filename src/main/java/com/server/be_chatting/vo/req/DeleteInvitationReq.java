package com.server.be_chatting.vo.req;

import lombok.Data;

@Data
public class DeleteInvitationReq {
    private Long invitationId;
    private Long userId;
}
