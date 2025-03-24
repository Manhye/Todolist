package com.example.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorInfoDto {
    private String email;
    private String password;
}
