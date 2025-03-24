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


    public TodolistResponseDto(Todolist todolist){
        this.id=todolist.getId();
        this.task=todolist.getTask();
        this.description=todolist.getDescription();
        this.scheduled_date=todolist.getScheduled_date();
        this.created_at=todolist.getCreated_at();
        this.updated_at=todolist.getUpdated_at();
    }
}
