package com.server.be_chatting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.InvitationInfo;

@Mapper
public interface InvitationRepository extends BaseMapper<InvitationInfo> {
    @Select("select * from invitation_info where deleted = 0")
    List<InvitationInfo> selectListAll();

    @Select("select * from invitation_info where invitation_id = #{invitationId} and user_id = #{userId} and deleted "
            + "= 0 limit 1")
    InvitationInfo selectByInvitationIdAndUserId(@Param("invitationId") Long invitationId,
            @Param("userId") Long userId);
}
