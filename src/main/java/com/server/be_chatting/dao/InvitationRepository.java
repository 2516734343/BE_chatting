package com.server.be_chatting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.InvitationInfo;

@Mapper
public interface InvitationRepository extends BaseMapper<InvitationInfo> {
    @Select("select * from invitation_info where deleted = 0")
    List<InvitationInfo> selectListAll();
}
