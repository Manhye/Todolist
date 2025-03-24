package com.example.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class FindListResponseDto {
    private int id;
    private String task;
    private String description;
    private Date scheduled_date;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String name;
    private String email;
}
