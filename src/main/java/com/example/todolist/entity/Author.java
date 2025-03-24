package com.example.todolist.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Author {
    private int id;
    private String name;
    private String email;
    private String password;
    private Timestamp created_at;
    private Timestamp updated_at;
}
