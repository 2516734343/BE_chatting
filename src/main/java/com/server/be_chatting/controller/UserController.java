package com.server.be_chatting.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.server.be_chatting.constant.ErrorCode;
import com.server.be_chatting.exception.ServiceException;
import com.server.be_chatting.service.UserService;
import com.server.be_chatting.vo.RestRsp;
import com.server.be_chatting.vo.UserVo;
import com.server.be_chatting.vo.req.AddTagReq;
import com.server.be_chatting.vo.req.DeleteTagReq;

@RestController
@RequestMapping("api/be/chatting")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("get/user/info")
    public RestRsp<UserVo> getUserInfo(Long userId) {
        if (userId == null || userId == 0) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "userId不能为空");
        }
        return RestRsp.success(userService.getUserInfo(userId));
    }

    @PostMapping("update/user/info")
    public RestRsp<Map<String, Object>> updateUserInfo(String username, Long userId, String password, String name,
            Integer sex, Integer age, String city, Integer emotion, String signature,
            @RequestParam("tagList") List<Long> tagList) {
        if (userId == null || userId == 0 || StringUtils.isEmpty(username) || StringUtils
                .isEmpty(password) || StringUtils.isEmpty(name)
                || sex == null || age == null || StringUtils.isEmpty(city) || emotion == null || StringUtils
                .isEmpty(signature)) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(userService
                .updateUserInfo(username, userId, password, name, sex, age, city, emotion, signature, tagList));
    }

    @PostMapping("update/user/photo")
    public RestRsp<Map<String, Object>> updateUserPhoto(@RequestParam("file") MultipartFile file, Long userId) {
        if (file == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(userService.updateUserPhoto(file, userId));
    }

    @PostMapping("tag/batch/add")
    public RestRsp<Map<String, Object>> addTag(@RequestBody AddTagReq addTagReq) {
        if (addTagReq.getUserId() == null || CollectionUtils.isEmpty(addTagReq.getName())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(userService.addTag(addTagReq));
    }

    @PostMapping("tag/delete")
    public RestRsp<Map<String, Object>> deleteTag(@RequestBody DeleteTagReq deleteTagReq) {
        if (deleteTagReq.getId() == null || deleteTagReq.getUserId() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(userService.deleteTag(deleteTagReq));
    }
}
