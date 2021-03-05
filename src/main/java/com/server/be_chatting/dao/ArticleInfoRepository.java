package com.server.be_chatting.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.ArticleInfo;


@Mapper
public interface ArticleInfoRepository extends BaseMapper<ArticleInfo> {
    @Select("select * from article_info where deleted = 0")
    List<ArticleInfo> getArticleList();

    @Select("select * from article_info where deleted = 0 and id = #{id} limit 1")
    ArticleInfo selectByArticleId(@Param("id") Long articleId);

}
