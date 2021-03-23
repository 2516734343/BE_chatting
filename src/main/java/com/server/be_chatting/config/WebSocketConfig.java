package com.server.be_chatting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.server.be_chatting.controller.WebSocketController;
import com.server.be_chatting.service.WebSocketService;

@Configuration
@EnableWebSocket
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    private void setWebSocketService(WebSocketService webSocketService) {
        WebSocketController.webSocketService = webSocketService;
    }
}
