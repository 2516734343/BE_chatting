package com.server.be_chatting.service;


import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.server.be_chatting.constant.ErrorCode;
import com.server.be_chatting.dao.ArticleInfoRepository;
import com.server.be_chatting.dao.CommentInfoRepository;
import com.server.be_chatting.dao.InvitationLikeRelationRepository;
import com.server.be_chatting.dao.InvitationRepository;
import com.server.be_chatting.dao.TagInfoRepository;
import com.server.be_chatting.dao.UserFriendRelationRepository;
import com.server.be_chatting.dao.UserInfoRepository;
import com.server.be_chatting.dao.UserTagRelationRepository;
import com.server.be_chatting.domain.ArticleInfo;
import com.server.be_chatting.domain.CommentInfo;
import com.server.be_chatting.domain.InvitationInfo;
import com.server.be_chatting.domain.InvitationLikeRelation;
import com.server.be_chatting.domain.TagInfo;
import com.server.be_chatting.domain.UserFriendRelation;
import com.server.be_chatting.domain.UserInfo;
import com.server.be_chatting.domain.UserTagRelation;
import com.server.be_chatting.dto.InvitationRelationDto;
import com.server.be_chatting.enums.DeleteStatusEnums;
import com.server.be_chatting.enums.UserAddFriendStatusEnums;
import com.server.be_chatting.exception.ServiceException;
import com.server.be_chatting.param.PageRequestParam;
import com.server.be_chatting.util.DateUtil;
import com.server.be_chatting.vo.CommentVo;
import com.server.be_chatting.vo.InvitationCommentTopFiveVo;
import com.server.be_chatting.vo.InvitationLikeTopFiveVo;
import com.server.be_chatting.vo.InvitationVo;
import com.server.be_chatting.vo.RestListData;
import com.server.be_chatting.vo.TagVo;
import com.server.be_chatting.vo.UserActionVo;
import com.server.be_chatting.vo.UserVo;
import com.server.be_chatting.vo.req.AddTagReq;
import com.server.be_chatting.vo.req.AddUserFriendReq;
import com.server.be_chatting.vo.req.DeleteInvitationReq;
import com.server.be_chatting.vo.req.DeleteTagReq;
import com.server.be_chatting.vo.req.InvitationCommentReq;
import com.server.be_chatting.vo.req.InvitationLikeReq;
import com.server.be_chatting.vo.req.ReleaseInvitationReq;
import com.server.be_chatting.vo.req.UserFriendApplyReq;

@Service
public class UserService {
    @Resource
    private UserInfoRepository userInfoRepository;
    @Resource
    private TagInfoRepository tagInfoRepository;
    @Resource
    private UserTagRelationRepository userTagRelationRepository;
    @Resource
    private ArticleInfoRepository articleInfoRepository;
    @Resource
    private InvitationRepository invitationRepository;
    @Resource
    private CommentInfoRepository commentInfoRepository;
    @Resource
    private InvitationLikeRelationRepository invitationLikeRelationRepository;
    @Resource
    private UserFriendRelationRepository userFriendRelationRepository;
    @Autowired
    private CommonService commonService;

    @Value("${server.port}")
    private String port;

    public UserVo getUserInfo(Long userId) {
        UserInfo userInfo = userInfoRepository.selectByUserId(userId);
        if (userInfo == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户不存在");
        }
        ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(userInfo.getArticleId());
        UserVo userVo = new UserVo();
        userVo.setUserId(userInfo.getId());
        userVo.setUsername(userInfo.getUsername());
        userVo.setName(userInfo.getName());
        userVo.setAge(userInfo.getAge());
        userVo.setCity(userInfo.getCity());
        userVo.setEmotion(userInfo.getEmotion());
        userVo.setPassword(userInfo.getPassword());
        userVo.setPhoto("localhost:" + port + "/" + articleInfo.getPath());
        userVo.setSex(userInfo.getSex());
        userVo.setSignature(userInfo.getSignature());
        List<TagVo> tagVoList = Lists.newArrayList();
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.selectByUserId(userInfo.getId());
        userTagRelationList.forEach(userTagRelation -> {
            TagVo tagVo = new TagVo();
            TagInfo tagInfo = tagInfoRepository.selectByTagId(userTagRelation.getTagId());
            tagVo.setTagId(tagInfo.getId());
            tagVo.setTagName(tagInfo.getName());
            tagVoList.add(tagVo);
        });
        userVo.setTagList(tagVoList);
        return userVo;
    }

    public Map<String, Object> updateUserInfo(String username,
            Long userId, String password, String name, Integer sex, Integer age, String city, Integer emotion,
            String signature, List<Long> tagList) {
        UserInfo userInfo = userInfoRepository.selectByUserId(userId);
        if (userInfo == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户不存在");
        }
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setName(name);
        userInfo.setSex(sex);
        userInfo.setAge(age);
        userInfo.setCity(city);
        userInfo.setEmotion(emotion);
        userInfo.setSignature(signature);
        userInfo.setUpdateTime(System.currentTimeMillis());
        userInfoRepository.updateById(userInfo);
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.selectByUserId(userId);
        if (!CollectionUtils.isEmpty(userTagRelationList)) {
            userTagRelationList.forEach(userTagRelation -> {
                userTagRelationRepository.deleteById(userTagRelation.getId());
            });
        }
        commonService.relationTag(tagList, userId);
        return Maps.newHashMap();
    }

    public Map<String, Object> updateUserPhoto(MultipartFile file, Long userId) {
        UserInfo userInfo = userInfoRepository.selectByUserId(userId);
        if (userInfo == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户不存在");
        }
        ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(userInfo.getArticleId());
        if (articleInfo == null) {
            return Maps.newHashMap();
        }
        String filePath = Objects.requireNonNull(
                Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("static/img")).getPath() + "/"
                + articleInfo.getPath();
        File oldFile = new File(filePath);
        boolean flag = oldFile.delete();
        if (flag) {
            articleInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
            articleInfoRepository.updateById(articleInfo);
        }
        commonService.uploadFile(file, userInfo);
        return Maps.newHashMap();
    }

    public Map<String, Object> addTag(@RequestBody AddTagReq addTagReq) {
        List<String> tagList = addTagReq.getName();
        UserInfo userInfo = userInfoRepository.selectByUserId(addTagReq.getUserId());
        if (!CollectionUtils.isEmpty(tagList)) {
            tagList.forEach(tag -> {
                TagInfo tagInfo = new TagInfo();
                tagInfo.setName(tag);
                tagInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
                tagInfo.setCreateTime(System.currentTimeMillis());
                tagInfo.setUpdateTime(System.currentTimeMillis());
                if (userInfo != null) {
                    tagInfo.setCreator(userInfo.getUsername());
                }
                tagInfoRepository.insert(tagInfo);
                UserTagRelation userTagRelation = new UserTagRelation();
                userTagRelation.setTagId(tagInfo.getId());
                userTagRelation.setUserId(addTagReq.getUserId());
                userTagRelation.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
                userTagRelation.setCreateTime(System.currentTimeMillis());
                userTagRelation.setUpdateTime(System.currentTimeMillis());
                userTagRelationRepository.insert(userTagRelation);
            });
        }
        return Maps.newHashMap();
    }

    public Map<String, Object> deleteTag(@RequestBody DeleteTagReq deleteTagReq) {
        TagInfo tagInfo = tagInfoRepository.selectByTagId(deleteTagReq.getId());
        if (tagInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "标签不存在");
        }
        UserInfo userInfo = userInfoRepository.selectByUserId(deleteTagReq.getUserId());
        if (userInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "用户不存在");
        }
        if (!userInfo.getUsername().equals(tagInfo.getCreator())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "对不起，您没有该标签的操作权限");
        }
        tagInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
        tagInfoRepository.updateById(tagInfo);
        UserTagRelation userTagRelation =
                userTagRelationRepository.selectByUserIdAndTagId(deleteTagReq.getUserId(), deleteTagReq.getId());
        if (userTagRelation != null) {
            userTagRelation.setDeleted(DeleteStatusEnums.DELETED.getCode());
            userTagRelationRepository.updateById(userTagRelation);
        }
        return Maps.newHashMap();
    }

    public RestListData<InvitationVo> getInvitationVoList(PageRequestParam pageRequestParam) {
        List<InvitationVo> invitationVoList = Lists.newArrayList();
        List<InvitationInfo> invitationInfoList = invitationRepository.selectListAll();
        if (CollectionUtils.isEmpty(invitationInfoList)) {
            return RestListData.create(invitationVoList.size(), invitationVoList);
        }
        invitationInfoList.forEach(invitationInfo -> {
            InvitationVo invitationVo = new InvitationVo();
            invitationVo.setId(invitationInfo.getId());
            invitationVo.setContent(invitationInfo.getContent());
            UserInfo userInfo = userInfoRepository.selectByUserId(invitationInfo.getUserId());
            if (userInfo != null) {
                invitationVo.setUserId(userInfo.getId());
                invitationVo.setUserName(userInfo.getUsername());
                invitationVo.setName(userInfo.getName());
                ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(userInfo.getArticleId());
                if (articleInfo != null) {
                    invitationVo.setPhoto("localhost:" + port + "/" + articleInfo.getPath());
                }
            }
            invitationVo.setTime(invitationInfo.getCreateTime());
            Integer likeNum = invitationLikeRelationRepository.selectLikeNumByInvitationId(invitationInfo.getId());
            Integer commentNum = commentInfoRepository.selectCommentNumByInvitationId(invitationInfo.getId());
            invitationVo.setLikeNum(likeNum);
            invitationVo.setCommentNum(commentNum);
            invitationVoList.add(invitationVo);
        });
        invitationVoList.sort((o1, o2) -> o2.getLikeNum().compareTo(o1.getLikeNum()));
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), invitationVoList.size());
        return RestListData.create(invitationVoList.size(), invitationVoList.subList(start, end));
    }

    public Map<String, Object> invitationLike(InvitationLikeReq invitationLikeReq) {
        InvitationLikeRelation oldInvitationLikeRelation = invitationLikeRelationRepository
                .selectByInvitationIdAndUserId(invitationLikeReq.getInvitationId(), invitationLikeReq.getUserId());
        if (oldInvitationLikeRelation != null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "该贴子已被点赞过");
        }
        InvitationLikeRelation invitationLikeRelation = new InvitationLikeRelation();
        invitationLikeRelation.setInvitationId(invitationLikeReq.getInvitationId());
        invitationLikeRelation.setUserId(invitationLikeReq.getUserId());
        invitationLikeRelation.setCreateTime(System.currentTimeMillis());
        invitationLikeRelation.setUpdateTime(System.currentTimeMillis());
        invitationLikeRelation.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        invitationLikeRelationRepository.insert(invitationLikeRelation);
        return Maps.newHashMap();
    }

    public Map<String, Object> commentInvitation(InvitationCommentReq invitationCommentReq) {
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setUserId(invitationCommentReq.getUserId());
        commentInfo.setInvitationId(invitationCommentReq.getInvitationId());
        commentInfo.setCreateTime(System.currentTimeMillis());
        commentInfo.setUpdateTime(System.currentTimeMillis());
        commentInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        commentInfo.setContent(invitationCommentReq.getContent());
        commentInfoRepository.insert(commentInfo);
        return Maps.newHashMap();
    }

    public Map<String, Object> deleteInvitation(DeleteInvitationReq deleteInvitationReq) {
        InvitationInfo invitationInfo = invitationRepository
                .selectByInvitationIdAndUserId(deleteInvitationReq.getInvitationId(), deleteInvitationReq.getUserId());
        if (invitationInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "帖子不存在");
        }
        invitationInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
        invitationRepository.updateById(invitationInfo);
        return Maps.newHashMap();
    }

    public InvitationVo getInvitationInfo(Long invitationId) {
        InvitationInfo invitationInfo = invitationRepository.selectById(invitationId);
        if (invitationInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "帖子不存在");
        }
        InvitationVo invitationVo = new InvitationVo();
        invitationVo.setId(invitationInfo.getId());
        invitationVo.setContent(invitationInfo.getContent());
        UserInfo userInfo = userInfoRepository.selectByUserId(invitationInfo.getUserId());
        if (userInfo != null) {
            invitationVo.setUserId(userInfo.getId());
            invitationVo.setName(userInfo.getName());
            invitationVo.setUserName(userInfo.getUsername());
            ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(userInfo.getArticleId());
            if (articleInfo != null) {
                invitationVo.setPhoto("localhost:" + port + "/" + articleInfo.getPath());
            }
        }
        invitationVo.setTime(invitationInfo.getCreateTime());
        Integer commentNum = commentInfoRepository.selectCommentNumByInvitationId(invitationInfo.getId());
        Integer likeNum = invitationLikeRelationRepository.selectLikeNumByInvitationId(invitationInfo.getId());
        invitationVo.setLikeNum(likeNum);
        invitationVo.setCommentNum(commentNum);
        return invitationVo;
    }

    public RestListData<CommentVo> getCommentList(PageRequestParam pageRequestParam, Long invitationId) {
        List<CommentVo> commentVoList = Lists.newArrayList();
        List<CommentInfo> commentInfoList = commentInfoRepository.selectByInvitationId(invitationId);
        if (CollectionUtils.isEmpty(commentInfoList)) {
            return RestListData.create(commentVoList.size(), commentVoList);
        }
        commentInfoList.forEach(commentInfo -> {
            CommentVo commentVo = new CommentVo();
            commentVo.setCommentId(commentInfo.getId());
            commentVo.setContent(commentInfo.getContent());
            commentVo.setUserId(commentInfo.getUserId());
            UserInfo userInfo = userInfoRepository.selectByUserId(commentInfo.getUserId());
            if (userInfo != null) {
                commentVo.setUserName(userInfo.getUsername());
                commentVo.setName(userInfo.getName());
                ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(userInfo.getArticleId());
                if (articleInfo != null) {
                    commentVo.setPhoto("localhost:" + port + "/" + articleInfo.getPath());
                }
            }
            commentVo.setTime(commentInfo.getCreateTime());
            commentVoList.add(commentVo);
        });
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), commentVoList.size());
        return RestListData.create(commentVoList.size(), commentVoList.subList(start, end));
    }

    public RestListData<UserVo> getInvitationLikeUserList(Long invitationId) {
        List<UserVo> userVoList = Lists.newArrayList();
        List<InvitationLikeRelation> invitationLikeRelationList =
                invitationLikeRelationRepository.selectByInvitationId(invitationId);
        if (CollectionUtils.isEmpty(invitationLikeRelationList)) {
            return RestListData.create(userVoList.size(), userVoList);
        }
        invitationLikeRelationList.forEach(invitationLikeRelation -> {
            UserVo userVo = getUserInfo(invitationLikeRelation.getUserId());
            userVoList.add(userVo);
        });
        return RestListData.create(userVoList.size(), userVoList);
    }

    public Map<String, Object> releaseInvitation(ReleaseInvitationReq releaseInvitationReq) {
        InvitationInfo invitationInfo = new InvitationInfo();
        invitationInfo.setContent(releaseInvitationReq.getContent());
        invitationInfo.setUserId(releaseInvitationReq.getUserId());
        invitationInfo.setCreateTime(System.currentTimeMillis());
        invitationInfo.setUpdateTime(System.currentTimeMillis());
        invitationInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        invitationRepository.insert(invitationInfo);
        return Maps.newHashMap();
    }

    public RestListData<InvitationLikeTopFiveVo> getInvitationLikeTopFive() {
        List<InvitationLikeTopFiveVo> invitationLikeTopFiveVoList = Lists.newArrayList();
        List<InvitationRelationDto> invitationRelationDtoList =
                invitationLikeRelationRepository.selectTopFive();
        if (CollectionUtils.isEmpty(invitationRelationDtoList)) {
            return RestListData.create(invitationLikeTopFiveVoList.size(), invitationLikeTopFiveVoList);
        }
        invitationRelationDtoList.forEach(invitationRelationDto -> {
            InvitationLikeTopFiveVo invitationLikeTopFiveVo = new InvitationLikeTopFiveVo();
            invitationLikeTopFiveVo.setId(invitationRelationDto.getInvitationId());
            invitationLikeTopFiveVo.setLikeNum(invitationRelationDto.getNum());
            InvitationInfo invitationInfo =
                    invitationRepository.selectById(invitationRelationDto.getInvitationId());
            if (invitationInfo != null) {
                invitationLikeTopFiveVo.setContent(invitationInfo.getContent());
            }
            invitationLikeTopFiveVoList.add(invitationLikeTopFiveVo);
        });
        return RestListData.create(invitationLikeTopFiveVoList.size(), invitationLikeTopFiveVoList);
    }

    public RestListData<InvitationCommentTopFiveVo> getInvitationCommentTopFive() {
        List<InvitationCommentTopFiveVo> invitationCommentTopFiveVoList = Lists.newArrayList();
        List<InvitationRelationDto> invitationRelationDtoList = commentInfoRepository.selectTopFive();
        if (CollectionUtils.isEmpty(invitationRelationDtoList)) {
            return RestListData.create(invitationCommentTopFiveVoList.size(), invitationCommentTopFiveVoList);
        }
        invitationRelationDtoList.forEach(invitationRelationDto -> {
            InvitationCommentTopFiveVo invitationCommentTopFiveVo = new InvitationCommentTopFiveVo();
            invitationCommentTopFiveVo.setId(invitationRelationDto.getInvitationId());
            invitationCommentTopFiveVo.setCommentNum(invitationRelationDto.getNum());
            InvitationInfo invitationInfo =
                    invitationRepository.selectById(invitationRelationDto.getInvitationId());
            if (invitationInfo != null) {
                invitationCommentTopFiveVo.setContent(invitationInfo.getContent());
            }
            invitationCommentTopFiveVoList.add(invitationCommentTopFiveVo);
        });
        return RestListData.create(invitationCommentTopFiveVoList.size(), invitationCommentTopFiveVoList);
    }

    public UserActionVo getUserActionList(Long userId) {
        UserActionVo userActionVo = new UserActionVo();
        List<Integer> likeNumList = Lists.newArrayList();
        List<Integer> commentNumList = Lists.newArrayList();
        List<Integer> releaseNumList = Lists.newArrayList();
        long time = System.currentTimeMillis();
        for (int i = 1; i <= 7; i++) {
            Integer likeNum = invitationLikeRelationRepository
                    .selectLikeNumByUserIdAndTime(userId, DateUtil.plusDays(time, -1), time);
            Integer commentNum =
                    commentInfoRepository.selectCommentNumByUserId(userId, DateUtil.plusDays(time, -1), time);
            Integer releaseNum = invitationRepository.selectByUserIdAndTime(userId, DateUtil.plusDays(time, -1), time);
            likeNumList.add(likeNum);
            commentNumList.add(commentNum);
            releaseNumList.add(releaseNum);
            time = DateUtil.plusDays(time, -1);
        }
        Collections.reverse(likeNumList);
        Collections.reverse(commentNumList);
        Collections.reverse(releaseNumList);
        userActionVo.setLikeNum(likeNumList);
        userActionVo.setCommentNum(commentNumList);
        userActionVo.setReleaseNum(releaseNumList);
        return userActionVo;
    }

    public RestListData<UserVo> getUserRecommendList(Long userId) {
        List<UserVo> userVoList = Lists.newArrayList();
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.selectByUserId(userId);
        if (CollectionUtils.isEmpty(userTagRelationList)) {
            return RestListData.create(userVoList.size(), userVoList);
        }
        List<Long> userTagIds = Lists.newArrayList();
        userTagRelationList.forEach(userTagRelation -> {
            userTagIds.add(userTagRelation.getTagId());
        });
        List<UserTagRelation> userTagRelations =
                userTagRelationRepository.selectByTagIdsAndOtherUserId(userTagIds, userId);
        Set<Long> userIds = Sets.newHashSet();
        if (CollectionUtils.isEmpty(userTagRelations)) {
            return RestListData.create(userVoList.size(), userVoList);
        }
        userTagRelations.forEach(userTagRelation -> {
            userIds.add(userTagRelation.getUserId());
        });
        userIds.forEach(id -> {
            UserVo userVo = getUserInfo(id);
            userVoList.add(userVo);
        });
        return RestListData.create(userVoList.size(), userVoList);
    }

    public Map<String, Object> addUserFriend(AddUserFriendReq addUserFriendReq) {
        UserFriendRelation userFriendRelation = new UserFriendRelation();
        UserFriendRelation friendRelation = userFriendRelationRepository
                .selectByUserIdAndTargetUserId(addUserFriendReq.getUserId(), addUserFriendReq.getTargetUserId());
        if (friendRelation != null && friendRelation.getStatus().equals(UserAddFriendStatusEnums.APPLY.getCode())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "当前已经处于申请中");
        } else if (friendRelation != null && friendRelation.getStatus()
                .equals(UserAddFriendStatusEnums.REJECT.getCode())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "当前用户拒绝添加您为好友");
        } else if (friendRelation != null && friendRelation.getStatus()
                .equals(UserAddFriendStatusEnums.PASS.getCode())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "当前用户已经是您的好友");
        }
        userFriendRelation.setUserId(addUserFriendReq.getUserId());
        userFriendRelation.setTargetUserId(addUserFriendReq.getTargetUserId());
        userFriendRelation.setCreateTime(System.currentTimeMillis());
        userFriendRelation.setUpdateTime(System.currentTimeMillis());
        userFriendRelation.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        userFriendRelation.setStatus(UserAddFriendStatusEnums.APPLY.getCode());
        userFriendRelation.setContent(addUserFriendReq.getContent());
        userFriendRelationRepository.insert(userFriendRelation);
        return Maps.newHashMap();
    }

    public RestListData<UserVo> searchUserFriend(String search) {
        search = search == null ? StringUtils.EMPTY : search;
        List<UserVo> userVoList = Lists.newArrayList();
        List<UserInfo> userInfoList = userInfoRepository.selectUserList();
        if (CollectionUtils.isEmpty(userInfoList)) {
            return RestListData.create(userVoList.size(), userVoList);
        }
        String finalSearch = search;
        userInfoList.forEach(userInfo -> {
            UserVo userVo = getUserInfo(userInfo.getId());
            if (StringUtils.containsIgnoreCase(userVo.getName(), finalSearch) || StringUtils
                    .containsIgnoreCase(userVo.getUsername(), finalSearch)) {
                userVoList.add(userVo);
            }
        });
        return RestListData.create(userVoList.size(), userVoList);
    }

    public RestListData<UserVo> getUserFriendList(Long userId) {
        List<UserFriendRelation> userFriendRelations = userFriendRelationRepository.selectUserFriendByUserId(userId);
        List<UserVo> userVoList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(userFriendRelations)) {
            return RestListData.create(userVoList.size(), userVoList);
        }
        userFriendRelations.forEach(userFriendRelation -> {
            UserVo userVo = getUserInfo(userFriendRelation.getUserId());
            if (userVo != null) {
                userVo.setRecordId(userFriendRelation.getId());
                userVoList.add(userVo);
            }
        });
        return RestListData.create(userVoList.size(), userVoList);
    }

    public RestListData<UserVo> getUserFriendApplyList(Long userId) {
        List<UserVo> userVoList = Lists.newArrayList();
        List<UserFriendRelation> userFriendRelations =
                userFriendRelationRepository.selectUserApplyFriendByUserId(userId);
        if (CollectionUtils.isEmpty(userFriendRelations)) {
            return RestListData.create(userVoList.size(), userVoList);
        }
        userFriendRelations.forEach(userFriendRelation -> {
            UserVo userVo = getUserInfo(userFriendRelation.getUserId());
            if (userVo != null) {
                userVo.setRecordId(userFriendRelation.getId());
                userVo.setReason(userFriendRelation.getContent());
                userVoList.add(userVo);
            }
        });
        return RestListData.create(userVoList.size(), userVoList);
    }

    public Map<String, Object> applyUserFriend(UserFriendApplyReq userFriendApplyReq) {
        UserFriendRelation userFriendRelation =
                userFriendRelationRepository.selectByRecordId(userFriendApplyReq.getRecordId());
        if (userFriendRelation == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "该申请不存在");
        }
        userFriendRelation.setStatus(userFriendApplyReq.getStatus());
        userFriendRelationRepository.updateById(userFriendRelation);
        return Maps.newHashMap();
    }

}
