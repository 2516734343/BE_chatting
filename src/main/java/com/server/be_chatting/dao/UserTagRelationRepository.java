package com.server.be_chatting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.UserTagRelation;

@Mapper
public interface UserTagRelationRepository extends BaseMapper<UserTagRelation> {
    @Select("select * from user_tag_relation where user_id = #{userId} and deleted = 0")
    List<UserTagRelation> selectByUserId(@Param("userId") Long userId);

    @Select("select * from user_tag_relation where user_id = #{userId} and tag_id = #{tagId} and deleted = 0 limit 1")
    UserTagRelation selectByUserIdAndTagId(@Param("userId") Long userId, @Param("tagId") Long tagId);
}
