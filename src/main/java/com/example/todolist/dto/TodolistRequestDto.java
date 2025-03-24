package com.example.todolist.dto;

import lombok.Getter;

import java.sql.Date;

@Getter
public class TodolistRequestDto {
    private String task;
    private String description;
    private String name;
    private String email;
    private String password;
    private Date scheduled_date;
}
