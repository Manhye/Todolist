package com.example.todolist.exception;

public class TodolistNotFoundException extends RuntimeException {
    public TodolistNotFoundException(String message) {
        super(message);
    }
}