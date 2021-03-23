package com.server.be_chatting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.ChatRecordInfo;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-03-23
 */
@Mapper
public interface ChatRecordInfoRepository extends BaseMapper<ChatRecordInfo> {
    @Select("select * from chat_record_info where user_id = {userId} and target_user_id = #{targetUserId} and deleted"
            + " = 0 ")
    List<ChatRecordInfo> selectByUserIdAndTargetUserId(@Param("userId") Long userId,
            @Param("targetUserId") Long targetUserId);
}
