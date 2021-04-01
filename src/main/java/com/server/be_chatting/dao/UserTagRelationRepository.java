package com.server.be_chatting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.be_chatting.domain.UserTagRelation;
import com.server.be_chatting.dto.UserTagDto;

@Mapper
public interface UserTagRelationRepository extends BaseMapper<UserTagRelation> {
    @Select("select * from user_tag_relation where user_id = #{userId} and deleted = 0")
    List<UserTagRelation> selectByUserId(@Param("userId") Long userId);

    @Select("select * from user_tag_relation where user_id = #{userId} and tag_id = #{tagId} and deleted = 0 limit 1")
    UserTagRelation selectByUserIdAndTagId(@Param("userId") Long userId, @Param("tagId") Long tagId);

    @Select("<script> select * from user_tag_relation where deleted = 0 and tag_id in <foreach collection ='ids' "
            + "item='id' open='(' separator=',' close=')'> #{id} </foreach> and user_id != #{userId} </script>")
    List<UserTagRelation> selectByTagIdsAndOtherUserId(@Param("ids") List<Long> tagIds, @Param("userId") Long userId);

    @Select("select count(*) as value,tag_id as id from user_tag_relation group by tag_id,deleted having deleted = 0")
    List<UserTagDto> selectByTag();

}
