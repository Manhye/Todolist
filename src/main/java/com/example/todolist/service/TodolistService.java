package com.example.todolist.service;

import com.example.todolist.dto.TodolistRequestDto;
import com.example.todolist.dto.TodolistResponseDto;

import java.sql.Date;
import java.util.List;

public interface TodolistService {
    TodolistResponseDto saveTodolist(TodolistRequestDto dto);

    List<TodolistResponseDto> findAllTodolists();

    List<TodolistResponseDto> findTodolistByScheduled_date(Date scheduled_date);

    TodolistResponseDto findTodolistById(int id);

    TodolistResponseDto updateTodolist(int id, TodolistRequestDto dto);

    void deleteTodolist(int id);
}
