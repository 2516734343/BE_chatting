package com.server.be_chatting.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;


@Data
public class TagInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer deleted;
    private String creator;
    private Long createTime;
    private Long updateTime;
}
