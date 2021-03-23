package com.server.be_chatting.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-03-23
 */
@Data
public class ChatRecordVo {
    private Long recordId;
    private Long userId;
    private Long targetUserId;
    private String content;
}
