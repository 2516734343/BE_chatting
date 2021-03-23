package com.server.be_chatting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.UserFriendRelation;


@Mapper
public interface UserFriendRelationRepository extends BaseMapper<UserFriendRelation> {
    @Select("select * from user_friend_relation where user_id = #{userId} and target_user_id = #{targetUserId} and deleted = 0 limit 1")
    UserFriendRelation selectByUserIdAndTargetUserId(@Param("userId") Long userId,
            @Param("targetUserId") Long targetUserId);

    @Select("select * from user_friend_relation where user_id = #{userId} and target_user_id = #{targetUserId} and deleted = 0 and status = 3 limit 1")
    UserFriendRelation selectApplyByUserIdAndTargetUserId(@Param("userId") Long userId,
            @Param("targetUserId") Long targetUserId);

    @Select("select * from user_friend_relation where (user_id = #{userId} or target_user_id = #{userId}) and deleted = 0 and status = 1")
    List<UserFriendRelation> selectUserFriendByUserId(@Param("userId") Long userId);

    @Select("select * from user_friend_relation where target_user_id = #{userId} and deleted = 0 and status = 3")
    List<UserFriendRelation> selectUserApplyFriendByUserId(@Param("userId") Long userId);

    @Select("select * from user_friend_relation where id = #{recordId} and deleted = 0 limit 1")
    UserFriendRelation selectByRecordId(@Param("recordId") Long recordId);
}
