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
import com.server.be_chatting.param.PageRequestParam;
import com.server.be_chatting.service.UserService;
import com.server.be_chatting.service.WebSocketService;
import com.server.be_chatting.vo.ChatRecordVo;
import com.server.be_chatting.vo.CommentVo;
import com.server.be_chatting.vo.InvitationCommentTopFiveVo;
import com.server.be_chatting.vo.InvitationLikeTopFiveVo;
import com.server.be_chatting.vo.InvitationVo;
import com.server.be_chatting.vo.RestListData;
import com.server.be_chatting.vo.RestRsp;
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

@RestController
@RequestMapping("api/be/chatting")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private WebSocketService webSocketService;

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

    @GetMapping("invitation/list")
    public RestRsp<RestListData<InvitationVo>> getInvitationList(PageRequestParam pageRequestParam) {
        return RestRsp.success(userService.getInvitationVoList(pageRequestParam));
    }

    @PostMapping("invitation/like")
    public RestRsp<Map<String, Object>> invitationLike(@RequestBody InvitationLikeReq invitationLikeReq) {
        if (invitationLikeReq.getInvitationId() == null || invitationLikeReq.getUserId() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户ID或帖子ID不能为空");
        }
        return RestRsp.success(userService.invitationLike(invitationLikeReq));
    }

    @PostMapping("invitation/comment")
    public RestRsp<Map<String, Object>> commentInvitation(@RequestBody InvitationCommentReq invitationCommentReq) {
        if (invitationCommentReq.getInvitationId() == null || invitationCommentReq.getUserId() == null || StringUtils
                .isEmpty(invitationCommentReq.getContent())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(userService.commentInvitation(invitationCommentReq));
    }

    @PostMapping("invitation/delete")
    public RestRsp<Map<String, Object>> deleteInvitation(@RequestBody DeleteInvitationReq deleteInvitationReq) {
        if (deleteInvitationReq.getInvitationId() == null || deleteInvitationReq.getUserId() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(userService.deleteInvitation(deleteInvitationReq));
    }

    @GetMapping("invitation/info")
    public RestRsp<InvitationVo> getInvitationInfo(Long invitationId) {
        if (invitationId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "帖子ID不能为空");
        }
        return RestRsp.success(userService.getInvitationInfo(invitationId));
    }

    @GetMapping("invitation/comment/list")
    public RestRsp<RestListData<CommentVo>> getCommentList(PageRequestParam pageRequestParam, Long invitationId) {
        if (invitationId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "帖子ID不能为空");
        }
        return RestRsp.success(userService.getCommentList(pageRequestParam, invitationId));
    }

    @GetMapping("invitation/like/user/list")
    public RestRsp<RestListData<UserVo>> getInvitationLikeUserList(Long invitationId) {
        if (invitationId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "帖子ID不能为空");
        }
        return RestRsp.success(userService.getInvitationLikeUserList(invitationId));
    }

    @PostMapping("invitation/release")
    public RestRsp<Map<String, Object>> releaseInvitation(@RequestBody ReleaseInvitationReq releaseInvitationReq) {
        if (releaseInvitationReq.getUserId() == null || StringUtils.isEmpty(releaseInvitationReq.getContent())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(userService.releaseInvitation(releaseInvitationReq));
    }

    @GetMapping("invitation/like/top/five/list")
    public RestRsp<RestListData<InvitationLikeTopFiveVo>> getInvitationLikeTopFive() {
        return RestRsp.success(userService.getInvitationLikeTopFive());
    }

    @GetMapping("invitation/comment/top/five/list")
    public RestRsp<RestListData<InvitationCommentTopFiveVo>> getInvitationCommentTopFive() {
        return RestRsp.success(userService.getInvitationCommentTopFive());
    }

    @GetMapping("invitation/user/action/list")
    public RestRsp<UserActionVo> getUserActionList(Long userId) {
        if (userId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "userId不能为空");
        }
        return RestRsp.success(userService.getUserActionList(userId));
    }

    @GetMapping("user/recommend/list")
    public RestRsp<RestListData<UserVo>> getUserRecommendList(Long userId) {
        if (userId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "userId不能为空");
        }
        return RestRsp.success(userService.getUserRecommendList(userId));
    }

    @PostMapping("user/friend/add")
    public RestRsp<Map<String, Object>> addUserFriend(@RequestBody AddUserFriendReq addUserFriendReq) {
        if (addUserFriendReq.getUserId() == null || addUserFriendReq.getTargetUserId() == null || StringUtils
                .isEmpty(addUserFriendReq.getContent())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(userService.addUserFriend(addUserFriendReq));
    }

    @GetMapping("user/friend/find")
    public RestRsp<RestListData<UserVo>> searchUserFriend(String search) {
        return RestRsp.success(userService.searchUserFriend(search));
    }

    @GetMapping("user/friend/list")
    public RestRsp<RestListData<UserVo>> getUserFriendList(Long userId) {
        if (userId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(userService.getUserFriendList(userId));
    }

    @GetMapping("user/friend/apply/list")
    public RestRsp<RestListData<UserVo>> getUserFriendApplyList(Long userId) {
        if (userId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(userService.getUserFriendApplyList(userId));
    }

    @PostMapping("user/friend/apply")
    public RestRsp<Map<String, Object>> applyUserFriend(@RequestBody UserFriendApplyReq userFriendApplyReq) {
        if (userFriendApplyReq.getRecordId() == null || userFriendApplyReq.getStatus() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "传参错误");
        }
        return RestRsp.success(userService.applyUserFriend(userFriendApplyReq));
    }

    @GetMapping("user/chat/record/list")
    public RestRsp<RestListData<ChatRecordVo>> getChatRecordList(Long userId, Long targetUserId) {
        if (userId == null || targetUserId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "传参错误");
        }
        return RestRsp
                .success(webSocketService.getUserRecordList(userId, targetUserId));
    }


}
