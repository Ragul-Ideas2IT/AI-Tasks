package com.connectinghands.repository;

import com.connectinghands.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUserId(Long userId);

    Optional<Todo> findByIdAndUserId(int todoId, Long userId);
} 