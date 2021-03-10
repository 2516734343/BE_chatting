package com.server.be_chatting.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.CommentInfo;


@Mapper
public interface CommentInfoRepository extends BaseMapper<CommentInfo> {
    @Select("select count(*) from comment_info where invitation_id = #{invitationId} and deleted = 0")
    Integer selectCommentNumByInvitationId(@Param("invitationId") Long invitationId);

    @Select("select count(*) from comment_info where user_id = #{userId} and deleted = 0")
    Integer selectCommentNumByUserId(@Param("userId") Long userId);
}
