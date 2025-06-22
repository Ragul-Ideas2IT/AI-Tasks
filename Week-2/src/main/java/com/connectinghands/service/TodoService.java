package com.connectinghands.service;

import com.connectinghands.dto.TodoDto;

import java.util.List;

public interface TodoService {
    TodoDto addTodo(TodoDto todoDto);
    TodoDto getTodo(Integer todoId);
    List<TodoDto> getAllTodos();
    TodoDto updateTodo(Integer todoId, TodoDto todoDto);
    void deleteTodo(Integer todoId);
    TodoDto completeTodo(Integer todoId);
    TodoDto inCompleteTodo(Integer todoId);
} 