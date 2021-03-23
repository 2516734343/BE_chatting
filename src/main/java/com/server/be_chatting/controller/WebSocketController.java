package com.server.be_chatting.controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.server.be_chatting.dto.ChatMsgDto;
import com.server.be_chatting.service.WebSocketService;
import com.server.be_chatting.util.ObjectMapperUtils;

@RestController
@ServerEndpoint(value = "/api/be/chatting/websocket/{userId}")
public class WebSocketController {
    @Autowired
    private WebSocketService webSocketService;
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static ConcurrentHashMap<String, WebSocketController> webSocketSet = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session webSocketSession;
    //当前发消息的人员编号
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     * <p>
     * session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userId") String userId, Session webSocketSession) {
        this.userId = userId;//接收到发送消息的人员编号
        this.webSocketSession = webSocketSession;
        webSocketSet.put(userId, this);//加入map中
        addOnlineCount();     //在线数加1
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (!userId.equals("")) {
            webSocketSet.remove(userId); //从set中删除
            subOnlineCount();     //在线数减1
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param chatMsg 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String chatMsg, Session session) {
        //给指定的人发消息
        ChatMsgDto chatMsgDto = ObjectMapperUtils.fromJSON(chatMsg, ChatMsgDto.class);
        sendToUser(chatMsgDto);
        //sendAll(message);
    }

    /**
     * 给指定的人发送消息
     *
     * @param chatMsg 消息对象
     */
    public void sendToUser(ChatMsgDto chatMsg) {
        String targetUserId = chatMsg.getTargetUserId().toString();
        try {
            if (webSocketSet.get(targetUserId) != null) {
                webSocketSet.get(targetUserId).sendMessage(ObjectMapperUtils.toJSON(chatMsg));
            } else {
                webSocketSet.get(targetUserId).sendMessage("0" + "|" + "当前用户不在线");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            webSocketService.insertChatRecord(chatMsg);
        }
    }

    public void sendMessage(String message) throws IOException {
        this.webSocketSession.getBasicRemote().sendText(message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public static synchronized void addOnlineCount() {
        WebSocketController.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketController.onlineCount--;
    }
}
