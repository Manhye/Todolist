package com.example.todolist.service;

import com.example.todolist.dto.DeleteListRequestDto;
import com.example.todolist.dto.FindListResponseDto;
import com.example.todolist.dto.TodolistRequestDto;
import com.example.todolist.dto.TodolistResponseDto;

import java.sql.Date;
import java.util.List;

public interface TodolistService {
    TodolistResponseDto saveTodolist(TodolistRequestDto dto);

    List<FindListResponseDto> findAllTodolists();

    List<FindListResponseDto> findTodolistByScheduled_date(Date scheduled_date);

    FindListResponseDto findTodolistById(int id);

    FindListResponseDto updateTodolist(int id, TodolistRequestDto dto);

    void deleteTodolist(int id, DeleteListRequestDto dto);

    List<FindListResponseDto> findTodolistByPage(int page, int size);

    List<FindListResponseDto> findTodolistByUserId(int id);
}
