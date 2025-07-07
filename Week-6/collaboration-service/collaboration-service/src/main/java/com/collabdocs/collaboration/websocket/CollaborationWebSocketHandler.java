package com.collabdocs.collaboration.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class CollaborationWebSocketHandler extends TextWebSocketHandler {
    // Map<documentId, Set of sessions>
    private final ConcurrentHashMap<String, Set<WebSocketSession>> documentSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String documentId = getDocumentId(session);
        documentSessions.computeIfAbsent(documentId, k -> new CopyOnWriteArraySet<>()).add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String documentId = getDocumentId(session);
        Set<WebSocketSession> sessions = documentSessions.get(documentId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                documentSessions.remove(documentId);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String documentId = getDocumentId(session);
        Set<WebSocketSession> sessions = documentSessions.get(documentId);
        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen() && !s.getId().equals(session.getId())) {
                    s.sendMessage(message);
                }
            }
        }
    }

    private String getDocumentId(WebSocketSession session) {
        String path = session.getUri().getPath();
        // Path: /ws/collaborate/{documentId}
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    public java.util.Set<String> getSessionIdsForDocument(String documentId) {
        java.util.Set<org.springframework.web.socket.WebSocketSession> sessions = documentSessions.get(documentId);
        if (sessions == null) return java.util.Collections.emptySet();
        java.util.Set<String> ids = new java.util.HashSet<>();
        for (org.springframework.web.socket.WebSocketSession s : sessions) {
            ids.add(s.getId());
        }
        return ids;
    }
} 