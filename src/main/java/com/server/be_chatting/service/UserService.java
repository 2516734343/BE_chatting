package com.server.be_chatting.service;


import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.be_chatting.constant.ErrorCode;
import com.server.be_chatting.dao.ArticleInfoRepository;
import com.server.be_chatting.dao.TagInfoRepository;
import com.server.be_chatting.dao.UserInfoRepository;
import com.server.be_chatting.dao.UserTagRelationRepository;
import com.server.be_chatting.domain.ArticleInfo;
import com.server.be_chatting.domain.TagInfo;
import com.server.be_chatting.domain.UserInfo;
import com.server.be_chatting.domain.UserTagRelation;
import com.server.be_chatting.enums.DeleteStatusEnums;
import com.server.be_chatting.exception.ServiceException;
import com.server.be_chatting.vo.TagVo;
import com.server.be_chatting.vo.UserVo;
import com.server.be_chatting.vo.req.AddTagReq;
import com.server.be_chatting.vo.req.DeleteTagReq;

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


}
