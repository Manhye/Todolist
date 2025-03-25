package com.example.todolist.dto;

import com.example.todolist.entity.Todolist;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class TodolistResponseDto {
    private int id;
    private String task;
    private String description;
    private Date scheduled_date;
    private Timestamp created_at;
    private Timestamp updated_at;

}
