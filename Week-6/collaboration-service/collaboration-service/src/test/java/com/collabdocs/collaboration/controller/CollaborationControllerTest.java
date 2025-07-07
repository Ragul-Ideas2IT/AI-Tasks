package com.collabdocs.collaboration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CollaborationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetActiveCollaborators() throws Exception {
        mockMvc.perform(get("/api/collaboration/active/testdoc"))
                .andExpect(status().isOk());
    }
} 