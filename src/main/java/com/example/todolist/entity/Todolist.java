package com.example.todolist.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Todolist {
    private int id;
    private String task;
    private String description;
    private String author;
    private String password;
    private Date scheduled_date;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Todolist(String task, String description, String author, String password, Date scheduled_date){
        this.task = task;
        this.description = description;
        this.author = author;
        this.password = password;
        this.scheduled_date = scheduled_date;
    }

    public Todolist(int id, String task, String description, String author, Date scheduled_date, Timestamp created_at, Timestamp updated_at){
        this.id = id;
        this.task = task;
        this.description = description;
        this.author = author;
        this.scheduled_date = scheduled_date;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


}
