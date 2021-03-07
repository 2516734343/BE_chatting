package com.server.be_chatting.vo.req;

import lombok.Data;

@Data
public class DeleteTagReq {
    private Long id;
    private Long userId;
}
