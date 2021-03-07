package com.server.be_chatting.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.server.be_chatting.service.CommonService;
import com.server.be_chatting.vo.LoginVo;
import com.server.be_chatting.vo.RestListData;
import com.server.be_chatting.vo.RestRsp;
import com.server.be_chatting.vo.TagVo;
import com.server.be_chatting.vo.req.LoginReq;

@RestController
@RequestMapping("api/be/chatting/common")
@CrossOrigin
public class CommonController {

    @Autowired
    private CommonService commonService;

    @PostMapping("login")
    public RestRsp<LoginVo> login(@RequestBody LoginReq loginReq) {
        if (StringUtils.isEmpty(loginReq.getUsername()) || StringUtils.isEmpty(loginReq.getPassword())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户名或密码不能为空");
        }
        return RestRsp.success(commonService.login(loginReq.getUsername(), loginReq.getPassword()));
    }

    @PostMapping("logout")
    public RestRsp<Map<String, Object>> logout(@RequestBody LoginReq loginReq) {
        if (StringUtils.isEmpty(loginReq.getUsername())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户名不能为空");
        }
        return RestRsp.success(commonService.logout(loginReq.getUsername()));
    }

    @GetMapping("tag/List")
    public RestRsp<RestListData<TagVo>> getTagList() {
        return RestRsp.success(commonService.getTagList());
    }

    @PostMapping("register")
    public RestRsp<Map<String, Object>> register(@RequestParam("file") MultipartFile file, String username,
            String password, String name, Integer sex, Integer age, String city, Integer emotion, String signature,
            @RequestParam("tagList") List<Long> tagList) {
        if (file == null || StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(name)
                || sex == null || age == null || StringUtils.isEmpty(city) || emotion == null || StringUtils
                .isEmpty(signature)) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(
                commonService.register(file, username, password, name, sex, age, city, emotion, signature, tagList));
    }
}
