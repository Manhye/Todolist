package com.example.todolist.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.sql.Date;

@Getter
public class TodolistRequestDto {

    @NotNull(message = "Task is required")
    @Size(max = 200, message = "Task must be less than 200 characters")
    private String task;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Scheduled date is required")
    private Date scheduled_date;
}
