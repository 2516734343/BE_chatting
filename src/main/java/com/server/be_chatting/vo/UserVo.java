package com.server.be_chatting.vo;

import java.util.List;

import lombok.Data;


@Data
public class UserVo {
    private Long recordId;
    private Long userId;
    private String username;
    private String name;
    private String password;
    private Integer sex; //0:男，1：女
    private Integer age;
    private String city;
    private Integer emotion;//1：单身，2：离婚，3：已婚，4：恋爱
    private String photo;
    private String signature;//个性签名
    private List<TagVo> tagList;
    private Long time;
}
