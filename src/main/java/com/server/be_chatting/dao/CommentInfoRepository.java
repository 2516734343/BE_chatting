package com.server.be_chatting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.CommentInfo;
import com.server.be_chatting.dto.InvitationRelationDto;


@Mapper
public interface CommentInfoRepository extends BaseMapper<CommentInfo> {
    @Select("select count(*) from comment_info where invitation_id = #{invitationId} and deleted = 0")
    Integer selectCommentNumByInvitationId(@Param("invitationId") Long invitationId);

    @Select("select * from comment_info where invitation_id = #{invitationId} and deleted = 0")
    List<CommentInfo> selectByInvitationId(@Param("invitationId") Long invitationId);

    @Select("SELECT invitation_id,count(*) as num FROM comment_info GROUP BY invitation_id ORDER BY num desc limit 5")
    List<InvitationRelationDto> selectTopFive();

    @Select("select count(*) from comment_info where user_id = #{userId} and deleted = 0 and create_time > "
            + "#{fromTime} and create_time < #{toTime}")
    Integer selectCommentNumByUserId(@Param("userId") Long userId, @Param("fromTime") Long fromTime,
            @Param("toTime") Long toTime);

    @Select("select * from comment_info where user_id = #{userId} and deleted = 0")
    List<CommentInfo> selectByUserId(@Param("userId") Long userId);
}
