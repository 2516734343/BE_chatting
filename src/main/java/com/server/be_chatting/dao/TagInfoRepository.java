package com.server.be_chatting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.TagInfo;

@Mapper
public interface TagInfoRepository extends BaseMapper<TagInfo> {
    @Select("select * from tag_info where deleted = 0")
    List<TagInfo> selectListAll();

    @Select("select * from tag_info where id = #{tagId} and deleted = 0 limit 1")
    TagInfo selectByTagId(@Param("tagId") Long tagId);
}
