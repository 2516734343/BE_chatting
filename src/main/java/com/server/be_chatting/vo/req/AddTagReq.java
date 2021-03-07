package com.server.be_chatting.vo.req;

import java.util.List;

import lombok.Data;

@Data
public class AddTagReq {
    private List<String> name;
    private Long userId;
}
