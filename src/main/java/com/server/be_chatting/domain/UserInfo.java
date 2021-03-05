package com.server.be_chatting.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class UserInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String username;
    private String name;
    private String password;
    private String token;
    private Integer type;//用户类型：1：普通用户，0：超级管理员
    private Integer sex; //0:男，1：女
    private Integer age;
    private String city;
    private Integer emotion;//1：单身，2：离婚，3：已婚，4：恋爱
    private String signature;//个性签名
    private Long articleId; //头像文件对应的ID
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
