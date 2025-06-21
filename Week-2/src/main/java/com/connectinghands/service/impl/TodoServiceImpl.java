package com.connectinghands.service.impl;

import com.connectinghands.dto.TodoDto;
import com.connectinghands.entity.Todo;
import com.connectinghands.exception.ResourceNotFoundException;
import com.connectinghands.repository.TodoRepository;
import com.connectinghands.service.TodoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<TodoDto> getAllTodos() {
        return todoRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public TodoDto getTodoById(Integer id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found with id " + id));
        return convertToDto(todo);
    }

    @Override
    public TodoDto createTodo(TodoDto todoDto) {
        Todo todo = convertToEntity(todoDto);
        return convertToDto(todoRepository.save(todo));
    }

    @Override
    public TodoDto updateTodo(Integer id, TodoDto todoDto) {
        Todo existingTodo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found with id " + id));
        existingTodo.setTitle(todoDto.getTitle());
        existingTodo.setDescription(todoDto.getDescription());
        existingTodo.setCompleted(todoDto.isCompleted());
        return convertToDto(todoRepository.save(existingTodo));
    }

    @Override
    public void deleteTodo(Integer id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo not found with id " + id);
        }
        todoRepository.deleteById(id);
    }

    private TodoDto convertToDto(Todo todo) {
        TodoDto todoDto = new TodoDto();
        BeanUtils.copyProperties(todo, todoDto);
        return todoDto;
    }

    private Todo convertToEntity(TodoDto todoDto) {
        Todo todo = new Todo();
        BeanUtils.copyProperties(todoDto, todo);
        return todo;
    }
} 