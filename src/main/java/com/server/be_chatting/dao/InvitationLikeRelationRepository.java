package com.server.be_chatting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.InvitationLikeRelation;
import com.server.be_chatting.dto.InvitationRelationDto;

@Mapper
public interface InvitationLikeRelationRepository extends BaseMapper<InvitationLikeRelation> {
    @Select("select count(*) from invitation_like_relation where invitation_id = #{invitationId} and deleted = 0")
    Integer selectLikeNumByInvitationId(@Param("invitationId") Long invitationId);

    @Select("select * from invitation_like_relation where user_id = #{userId} and deleted = 0")
    List<InvitationLikeRelation> selectLikeByUserId(@Param("userId") Long userId);

    @Select("select count(*) from invitation_like_relation where user_id = #{userId} and deleted = 0 and create_time "
            + "> #{fromTime} and create_time < #{toTime}")
    Integer selectLikeNumByUserIdAndTime(@Param("userId") Long userId, @Param("fromTime") Long fromTime,
            @Param("toTime") Long toTime);

    @Select("select * from invitation_like_relation where invitation_id = #{invitationId} and deleted = 0")
    List<InvitationLikeRelation> selectByInvitationId(@Param("invitationId") Long invitationId);

    @Select("select * from invitation_like_relation where invitation_id = #{invitationId} and user_id = #{userId} and"
            + " deleted = 0")
    InvitationLikeRelation selectByInvitationIdAndUserId(@Param("invitationId") Long invitationId,
            @Param("userId") Long userId);

    @Select("SELECT invitation_id,count(*) as num FROM invitation_like_relation GROUP BY invitation_id ORDER BY num "
            + "desc limit 5")
    List<InvitationRelationDto> selectTopFive();
}
