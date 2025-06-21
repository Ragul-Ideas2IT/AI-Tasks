package com.connectinghands.controller.user;

import com.connectinghands.dto.user.UserDto;
import com.connectinghands.security.JwtAuthenticationFilter;
import com.connectinghands.security.JwtTokenProvider;
import com.connectinghands.security.SecurityConfig;
import com.connectinghands.security.UserSecurity;
import com.connectinghands.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @MockBean
    private UserSecurity userSecurity;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    public void getUserById_shouldReturnUser_whenUserExists() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");

        when(userService.getUserById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    @WithMockUser
    public void getAllUsers_shouldReturnUserList() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        List<UserDto> users = Collections.singletonList(userDto);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateUser_shouldReturnUpdatedUser_whenUserIsAdmin() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("Updated");
        userDto.setEmail("updated@example.com");

        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void updateUser_shouldReturnUpdatedUser_whenUserIsSelf() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("Updated");
        userDto.setEmail("test@example.com");

        when(userSecurity.checkUser(any(), eq(1L))).thenReturn(true);
        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteUser_shouldReturnSuccessMessage_whenUserIsAdmin() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully!."));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteUser_shouldReturnForbidden_whenUserIsNotAdmin() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isForbidden());
    }
} 