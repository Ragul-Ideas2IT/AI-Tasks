package com.connectinghands.service.impl;

import com.connectinghands.dto.TodoDto;
import com.connectinghands.entity.Todo;
import com.connectinghands.entity.User;
import com.connectinghands.exception.ResourceNotFoundException;
import com.connectinghands.repository.TodoRepository;
import com.connectinghands.repository.user.UserRepository;
import com.connectinghands.service.TodoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new Todo and associates it with the currently authenticated user.
     * @param todoDto DTO containing the details of the todo to create.
     * @return The newly created TodoDto.
     */
    @Override
    public TodoDto addTodo(TodoDto todoDto) {
        User user = getCurrentUser();

        Todo todo = modelMapper.map(todoDto, Todo.class);
        todo.setUser(user);

        Todo savedTodo = todoRepository.save(todo);

        return modelMapper.map(savedTodo, TodoDto.class);
    }

    /**
     * Retrieves a single Todo.
     * If the current user is an admin, they can retrieve any todo.
     * Otherwise, the user can only retrieve a todo they own.
     * @param todoId The ID of the todo to retrieve.
     * @return The requested TodoDto.
     * @throws ResourceNotFoundException if the todo does not exist or the user does not have access.
     */
    @Override
    public TodoDto getTodo(Integer todoId) {
        User user = getCurrentUser();
        Todo todo;

        if (isAdmin(user)) {
            todo = todoRepository.findById(todoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));
        } else {
            todo = todoRepository.findByIdAndUserId(todoId, user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));
        }

        return modelMapper.map(todo, TodoDto.class);
    }

    /**
     * Retrieves all Todos.
     * If the current user is an admin, all todos in the system are returned.
     * Otherwise, only the todos owned by the current user are returned.
     * @return A list of TodoDto.
     */
    @Override
    public List<TodoDto> getAllTodos() {
        User user = getCurrentUser();
        List<Todo> todos;

        if (isAdmin(user)) {
            todos = todoRepository.findAll();
        } else {
            todos = todoRepository.findByUserId(user.getId());
        }

        return todos.stream()
                .map(todo -> modelMapper.map(todo, TodoDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing Todo.
     * A user must be the owner of the todo to update it. Admins can update any todo.
     * @param todoId The ID of the todo to update.
     * @param todoDto DTO containing the new details.
     * @return The updated TodoDto.
     */
    @Override
    public TodoDto updateTodo(Integer todoId, TodoDto todoDto) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));

        ensureUserIsOwner(todo.getUser().getId());

        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        todo.setCompleted(todoDto.isCompleted());

        Todo updatedTodo = todoRepository.save(todo);

        return modelMapper.map(updatedTodo, TodoDto.class);
    }

    /**
     * Deletes a Todo.
     * A user must be the owner of the todo to delete it. Admins can delete any todo.
     * @param todoId The ID of the todo to delete.
     */
    @Override
    public void deleteTodo(Integer todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));

        ensureUserIsOwner(todo.getUser().getId());

        todoRepository.delete(todo);
    }

    /**
     * Marks a Todo as complete.
     * A user must be the owner of the todo.
     * @param todoId The ID of the todo to complete.
     * @return The updated TodoDto.
     */
    @Override
    public TodoDto completeTodo(Integer todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));

        ensureUserIsOwner(todo.getUser().getId());

        todo.setCompleted(true);
        Todo updatedTodo = todoRepository.save(todo);

        return modelMapper.map(updatedTodo, TodoDto.class);
    }

    /**
     * Marks a Todo as incomplete.
     * A user must be the owner of the todo.
     * @param todoId The ID of the todo to mark as incomplete.
     * @return The updated TodoDto.
     */
    @Override
    public TodoDto inCompleteTodo(Integer todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));

        ensureUserIsOwner(todo.getUser().getId());

        todo.setCompleted(false);
        Todo updatedTodo = todoRepository.save(todo);

        return modelMapper.map(updatedTodo, TodoDto.class);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private void ensureUserIsOwner(Long ownerId) {
        User user = getCurrentUser();
        if (!isAdmin(user) && !user.getId().equals(ownerId)) {
            throw new org.springframework.security.access.AccessDeniedException("You are not the owner of this resource");
        }
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }
} 