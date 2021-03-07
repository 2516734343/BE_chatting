package com.server.be_chatting.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
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
import com.server.be_chatting.enums.LoginStatusEnums;
import com.server.be_chatting.enums.UserTypeEnums;
import com.server.be_chatting.exception.ServiceException;
import com.server.be_chatting.util.TokenUtil;
import com.server.be_chatting.vo.LoginVo;
import com.server.be_chatting.vo.RestListData;
import com.server.be_chatting.vo.TagVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommonService {

    @Resource
    private UserInfoRepository userInfoRepository;
    @Resource
    private TagInfoRepository tagInfoRepository;
    @Resource
    private ArticleInfoRepository articleInfoRepository;
    @Resource
    private UserTagRelationRepository userTagRelationRepository;

    public LoginVo login(String username, String password) {
        LoginVo loginVo = new LoginVo();
        UserInfo userInfo = userInfoRepository.selectByUserName(username);
        if (userInfo == null) {
            loginVo.setLoginStatus(LoginStatusEnums.FAIL.getCode());
        } else if (userInfo.getPassword().equals(password)) {
            if (StringUtils.isNotEmpty(userInfo.getToken())) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "当前用户已经登录");
            }
            loginVo.setUserId(userInfo.getId());
            loginVo.setUsername(username);
            loginVo.setLoginStatus(LoginStatusEnums.SUCCESS.getCode());
            String token = TokenUtil.getToken(username);
            loginVo.setAuthentication(username + " " + token);
            userInfo.setToken(username + " " + token);
            userInfoRepository.updateById(userInfo);
        } else {
            loginVo.setLoginStatus(LoginStatusEnums.FAIL.getCode());
        }
        return loginVo;
    }

    public Map<String, Object> logout(String username) {
        UserInfo userInfo = userInfoRepository.selectByUserName(username);
        if (userInfo == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户不存在");
        }
        userInfo.setToken(StringUtils.EMPTY);
        userInfoRepository.updateById(userInfo);
        return Maps.newHashMap();
    }

    public RestListData<TagVo> getTagList() {
        List<TagVo> tagVoList = Lists.newArrayList();
        List<TagInfo> tagInfoList = tagInfoRepository.selectListAll();
        if (CollectionUtils.isEmpty(tagInfoList)) {
            return RestListData.create(tagVoList.size(), tagVoList);
        }
        tagInfoList.forEach(tagInfo -> {
            TagVo tagVo = new TagVo();
            tagVo.setTagId(tagInfo.getId());
            tagVo.setTagName(tagInfo.getName());
            tagVo.setCreator(tagInfo.getCreator());
            tagVoList.add(tagVo);
        });
        return RestListData.create(tagVoList.size(), tagVoList);
    }

    public Map<String, Object> register(MultipartFile file, String username,
            String password, String name, Integer sex, Integer age, String city, Integer emotion, String signature,
            List<Long> tagList) {
        UserInfo userInfo = userInfoRepository.selectByUserName(username);
        if (userInfo != null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "当前用户已经存在");
        }
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUsername(username);
        newUserInfo.setPassword(password);
        newUserInfo.setName(name);
        newUserInfo.setType(UserTypeEnums.COMMON_USER.getCode());
        newUserInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        newUserInfo.setSex(sex);
        newUserInfo.setAge(age);
        newUserInfo.setCity(city);
        newUserInfo.setEmotion(emotion);
        newUserInfo.setSignature(signature);
        newUserInfo.setCreateTime(System.currentTimeMillis());
        newUserInfo.setUpdateTime(System.currentTimeMillis());
        userInfoRepository.insert(newUserInfo);
        uploadFile(file, newUserInfo);
        relationTag(tagList, newUserInfo.getId());
        return Maps.newHashMap();
    }

    public void uploadFile(MultipartFile file, UserInfo userInfo) {
        try {
            if (file.isEmpty()) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "文件不能为空");
            }
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "文件名不能为空");
            }
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径
            String filePath = Objects.requireNonNull(
                    Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("static/img")).getPath();
            File path = new File(filePath);
            File dest = new File(path.getAbsolutePath() + "/" + fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setName(fileName);
            articleInfo.setPath("/img/" + fileName);
            articleInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            articleInfo.setCreateTime(System.currentTimeMillis());
            articleInfo.setUpdateTime(System.currentTimeMillis());
            articleInfoRepository.insert(articleInfo);
            userInfo.setArticleId(articleInfo.getId());
            userInfoRepository.updateById(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void relationTag(List<Long> tagIdList, Long userId) {
        tagIdList.forEach(tagId -> {
            UserTagRelation userTagRelation = new UserTagRelation();
            userTagRelation.setUserId(userId);
            userTagRelation.setTagId(tagId);
            userTagRelation.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            userTagRelation.setCreateTime(System.currentTimeMillis());
            userTagRelation.setUpdateTime(System.currentTimeMillis());
            userTagRelationRepository.insert(userTagRelation);
        });
    }
}
