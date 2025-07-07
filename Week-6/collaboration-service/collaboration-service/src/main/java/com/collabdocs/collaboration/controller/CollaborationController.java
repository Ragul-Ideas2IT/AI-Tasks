package com.collabdocs.collaboration.controller;

import com.collabdocs.collaboration.websocket.CollaborationWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/collaboration")
public class CollaborationController {
    private final CollaborationWebSocketHandler webSocketHandler;

    @Autowired
    public CollaborationController(CollaborationWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping("/active/{documentId}")
    public int getActiveCollaborators(@PathVariable String documentId) {
        Set<String> sessionIds = webSocketHandler.getSessionIdsForDocument(documentId);
        return sessionIds.size();
    }
} 