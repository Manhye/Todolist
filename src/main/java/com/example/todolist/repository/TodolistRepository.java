package com.example.todolist.repository;

import com.example.todolist.dto.*;

import java.sql.Date;
import java.util.List;

public interface TodolistRepository {
    TodolistResponseDto saveTodolist(TodolistRequestDto dto);

    List<FindListResponseDto> findAllTodolists();

    List<FindListResponseDto> findTodolistByScheduled_date(Date scheduledDate);

    FindListResponseDto findTodolistByIdOrElseThrow(int id);

    int updatedTodolist(int id, String task, String description, Date scheduled_date);

    int deleteTodolist(int id, DeleteListRequestDto dto);

    List<FindListResponseDto> findTodolistByPage(int page, int size);

    AuthorInfoDto findEmailAndPasswordById(int id);

    List<FindListResponseDto> findTodolistByUserId(int id);
}
