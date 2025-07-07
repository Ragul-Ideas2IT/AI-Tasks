package com.collabdocs.collaboration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import com.collabdocs.collaboration.websocket.CollaborationWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final CollaborationWebSocketHandler collaborationWebSocketHandler;

    @Autowired
    public WebSocketConfig(CollaborationWebSocketHandler collaborationWebSocketHandler) {
        this.collaborationWebSocketHandler = collaborationWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(collaborationWebSocketHandler, "/ws/collaborate/*").setAllowedOrigins("*");
    }
} 