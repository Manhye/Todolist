package com.example.todolist.repository;

import com.example.todolist.dto.TodolistRequestDto;
import com.example.todolist.dto.TodolistResponseDto;
import com.example.todolist.entity.Todolist;

import java.sql.Date;
import java.util.List;

public interface TodolistRepository {
    TodolistResponseDto saveTodolist(Todolist todolist);

    List<TodolistResponseDto> findAllTodolists();

    List<TodolistResponseDto> findTodolistByScheduled_date(Date scheduledDate);

    Todolist findTodolistByIdOrElseThrow(int id);

    int updatedTodolist(int id, String task, String description, String author, Date scheduled_date);

    String findPasswordById(int id);

    int deleteTodolist(int id);
}
