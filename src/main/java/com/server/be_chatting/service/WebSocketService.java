package com.server.be_chatting.service;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.server.be_chatting.dao.ChatRecordInfoRepository;
import com.server.be_chatting.domain.ChatRecordInfo;
import com.server.be_chatting.dto.ChatMsgDto;
import com.server.be_chatting.enums.DeleteStatusEnums;
import com.server.be_chatting.vo.ChatRecordVo;
import com.server.be_chatting.vo.RestListData;

@Service
public class WebSocketService {
    @Resource
    private ChatRecordInfoRepository chatRecordInfoRepository;

    public void insertChatRecord(ChatMsgDto chatMsg) {
        ChatRecordInfo chatRecordInfo = new ChatRecordInfo();
        chatRecordInfo.setUserId(chatMsg.getUserId());
        chatRecordInfo.setTargetUserId(chatMsg.getTargetUserId());
        chatRecordInfo.setContent(chatMsg.getContent());
        chatRecordInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        chatRecordInfo.setCreateTime(System.currentTimeMillis());
        chatRecordInfo.setUpdateTime(System.currentTimeMillis());
        chatRecordInfoRepository.insert(chatRecordInfo);
    }

    public RestListData<ChatRecordVo> getUserRecordList(Long userId, Long targetUserId) {
        List<ChatRecordVo> chatRecordVoList = Lists.newArrayList();
        List<ChatRecordInfo> chatRecordInfoList = Lists.newArrayList();
        List<ChatRecordInfo> userChatRecordInfoList =
                chatRecordInfoRepository.selectByUserIdAndTargetUserId(userId, targetUserId);
        List<ChatRecordInfo> targetUserChatRecordInfoList =
                chatRecordInfoRepository.selectByUserIdAndTargetUserId(targetUserId, userId);
        if (!CollectionUtils.isEmpty(userChatRecordInfoList)) {
            chatRecordInfoList.addAll(userChatRecordInfoList);
        }
        if (!CollectionUtils.isEmpty(targetUserChatRecordInfoList)) {
            chatRecordInfoList.addAll(targetUserChatRecordInfoList);
        }
        if (CollectionUtils.isEmpty(chatRecordInfoList)) {
            return RestListData.create(chatRecordVoList.size(), chatRecordVoList);
        }
        chatRecordInfoList.sort(Comparator.comparing(ChatRecordInfo::getCreateTime));
        chatRecordInfoList.forEach(chatRecordInfo -> {
            ChatRecordVo chatRecordVo = new ChatRecordVo();
            chatRecordVo.setRecordId(chatRecordInfo.getId());
            chatRecordVo.setUserId(chatRecordInfo.getUserId());
            chatRecordVo.setTargetUserId(chatRecordInfo.getTargetUserId());
            chatRecordVo.setContent(chatRecordInfo.getContent());
            chatRecordVoList.add(chatRecordVo);
        });
        return RestListData.create(chatRecordVoList.size(), chatRecordVoList);
    }
}
