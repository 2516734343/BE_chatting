package com.server.be_chatting.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.InvitationLikeRelation;

@Mapper
public interface InvitationLikeRelationRepository extends BaseMapper<InvitationLikeRelation> {
    @Select("select count(*) from invitation_like_relation where invitation_id = #{invitationId} and deleted = 0")
    Integer selectLikeNumByInvitationId(@Param("invitationId") Long invitationId);

    @Select("select count(*) from invitation_like_relation where user_id = #{userId} and deleted = 0")
    Integer selectLikeNumByUserId(@Param("userId") Long userId);
}
