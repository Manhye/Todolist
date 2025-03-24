package com.example.todolist.repository;

import com.example.todolist.dto.DeleteListRequestDto;
import com.example.todolist.dto.FindListResponseDto;
import com.example.todolist.dto.TodolistRequestDto;
import com.example.todolist.dto.TodolistResponseDto;
import com.example.todolist.entity.Todolist;

import java.sql.Date;
import java.util.List;

public interface TodolistRepository {
    TodolistResponseDto saveTodolist(TodolistRequestDto dto);

    List<FindListResponseDto> findAllTodolists();

    List<FindListResponseDto> findTodolistByScheduled_date(Date scheduledDate);

    FindListResponseDto findTodolistByIdOrElseThrow(int id);

    int updatedTodolist(int id, String task, String description, Date scheduled_date);

    String findPasswordById(int id);

    String findEmailById(int id);

    int deleteTodolist(int id, DeleteListRequestDto dto);

    List<FindListResponseDto> findTodolistByPage(int page, int size);
}
