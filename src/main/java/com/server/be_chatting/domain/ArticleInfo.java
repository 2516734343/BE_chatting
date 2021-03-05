package com.server.be_chatting.domain;

import lombok.Data;

@Data
public class ArticleInfo {
    private Long id;
    private String name;
    private String path;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
