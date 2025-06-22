package com.connectinghands.service;

import com.connectinghands.dto.TodoDto;
import com.connectinghands.entity.Role;
import com.connectinghands.entity.Todo;
import com.connectinghands.entity.User;
import com.connectinghands.repository.TodoRepository;
import com.connectinghands.repository.user.UserRepository;
import com.connectinghands.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;
    private Authentication authentication;
    private SecurityContext securityContext;

    private User regularUser;
    private User adminUser;
    private Todo todo1;
    private TodoDto todoDto1;

    @BeforeEach
    void setUp() {
        // Mock Security Context
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

        // Setup Users
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        regularUser = new User();
        regularUser.setId(1L);
        regularUser.setEmail("user@example.com");
        regularUser.setRoles(Set.of(userRole));

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setEmail("admin@example.com");
        adminUser.setRoles(Set.of(adminRole));

        // Setup Todos
        todo1 = new Todo();
        todo1.setId(101);
        todo1.setTitle("User's Todo");
        todo1.setUser(regularUser);

        todoDto1 = new TodoDto();
        todoDto1.setId(101);
        todoDto1.setTitle("User's Todo");
        todoDto1.setUserId(regularUser.getId());
    }

    @AfterEach
    void tearDown() {
        mockedSecurityContextHolder.close();
    }

    private void mockCurrentUser(User user) {
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    @Test
    void getAllTodos_asUser_shouldReturnOnlyUserTodos() {
        mockCurrentUser(regularUser);
        when(todoRepository.findByUserId(regularUser.getId())).thenReturn(Collections.singletonList(todo1));
        when(modelMapper.map(any(Todo.class), eq(TodoDto.class))).thenReturn(todoDto1);

        List<TodoDto> result = todoService.getAllTodos();

        assertEquals(1, result.size());
        assertEquals(regularUser.getId(), result.get(0).getUserId());
        verify(todoRepository).findByUserId(regularUser.getId());
        verify(todoRepository, never()).findAll();
    }

    @Test
    void getAllTodos_asAdmin_shouldReturnAllTodos() {
        mockCurrentUser(adminUser);
        Todo otherTodo = new Todo();
        otherTodo.setUser(new User());
        when(todoRepository.findAll()).thenReturn(List.of(todo1, otherTodo));

        todoService.getAllTodos();

        verify(todoRepository).findAll();
        verify(todoRepository, never()).findByUserId(anyLong());
    }

    @Test
    void getTodo_asOwner_shouldReturnTodo() {
        mockCurrentUser(regularUser);
        when(todoRepository.findByIdAndUserId(todo1.getId(), regularUser.getId())).thenReturn(Optional.of(todo1));
        when(modelMapper.map(todo1, TodoDto.class)).thenReturn(todoDto1);

        TodoDto result = todoService.getTodo(todo1.getId());

        assertNotNull(result);
        assertEquals(todo1.getId(), result.getId());
    }

    @Test
    void getTodo_asAdmin_shouldReturnTodo() {
        mockCurrentUser(adminUser);
        when(todoRepository.findById(anyInt())).thenReturn(Optional.of(todo1));
        when(modelMapper.map(todo1, TodoDto.class)).thenReturn(todoDto1);

        TodoDto result = todoService.getTodo(todo1.getId());

        assertNotNull(result);
        assertEquals(todo1.getId(), result.getId());
        verify(todoRepository, never()).findByIdAndUserId(anyInt(), anyLong());
    }

    @Test
    void addTodo_shouldAssociateTodoWithCurrentUser() {
        mockCurrentUser(regularUser);
        when(modelMapper.map(any(TodoDto.class), eq(Todo.class))).thenReturn(todo1);
        when(todoRepository.save(any(Todo.class))).thenReturn(todo1);

        todoService.addTodo(new TodoDto());

        verify(todoRepository).save(argThat(todo -> todo.getUser().equals(regularUser)));
    }

    @Test
    void updateTodo_asNonOwner_shouldThrowAccessDenied() {
        // Another user who is not an admin
        User anotherUser = new User();
        anotherUser.setId(99L);
        anotherUser.setEmail("another@user.com");
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        anotherUser.setRoles(Set.of(userRole));
        mockCurrentUser(anotherUser);

        when(todoRepository.findById(todo1.getId())).thenReturn(Optional.of(todo1));

        assertThrows(AccessDeniedException.class, () -> {
            todoService.updateTodo(todo1.getId(), new TodoDto());
        });
    }

    @Test
    void deleteTodo_asOwner_shouldDeleteTodo() {
        mockCurrentUser(regularUser);
        when(todoRepository.findById(todo1.getId())).thenReturn(Optional.of(todo1));

        assertDoesNotThrow(() -> {
            todoService.deleteTodo(todo1.getId());
        });

        verify(todoRepository).delete(todo1);
    }
} 