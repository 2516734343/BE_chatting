package com.server.be_chatting.vo;

import java.util.List;

import lombok.Data;

@Data
public class UserActionVo {
    private List<Integer> likeNum;
    private List<Integer> commentNum;
    private List<Integer> releaseNum;
}
