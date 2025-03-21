package com.example.todolist.service;

import com.example.todolist.dto.TodolistRequestDto;
import com.example.todolist.dto.TodolistResponseDto;
import com.example.todolist.entity.Todolist;
import com.example.todolist.repository.TodolistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.List;

@Service
public class TodolistServiceImpl implements TodolistService{

    private final TodolistRepository todolistRepository;

    public TodolistServiceImpl(TodolistRepository todolistRepository){
        this.todolistRepository = todolistRepository;
    }

    @Override
    public TodolistResponseDto saveTodolist(TodolistRequestDto dto) {

        Todolist todolist = new Todolist(dto.getTask(), dto.getDescription(), dto.getAuthor(), dto.getPassword(), dto.getScheduled_date());

        return todolistRepository.saveTodolist(todolist);

    }

    @Override
    public List<TodolistResponseDto> findAllTodolists() {
        return todolistRepository.findAllTodolists();
    }

    @Override
    public List<TodolistResponseDto> findTodolistByScheduled_date(Date scheduled_date) {
        return todolistRepository.findTodolistByScheduled_date(scheduled_date);
    }

    @Override
    public TodolistResponseDto findTodolistById(int id) {
        Todolist todolist = todolistRepository.findTodolistByIdOrElseThrow(id);

        return new TodolistResponseDto(todolist);
    }


    @Transactional
    @Override
    public TodolistResponseDto updateTodolist(int id, TodolistRequestDto dto) {
        if(dto.getTask()==null||dto.getDescription()==null||dto.getAuthor()==null||dto.getPassword()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Task, description, author, and password are required");
        }

        String password = todolistRepository.findPasswordById(id);

        if (!password.equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }

        int updatedRow = todolistRepository.updatedTodolist(id, dto.getTask(),dto.getDescription(),dto.getAuthor(),dto.getScheduled_date());

        if(updatedRow==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = "+ id);
        }

        Todolist todolist = todolistRepository.findTodolistByIdOrElseThrow(id);


        return new TodolistResponseDto(todolist);
    }

    @Override
    public void deleteTodolist(int id) {
        int deletedRow = todolistRepository.deleteTodolist(id);
        if(deletedRow==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = "+ id);
        }
    }
}
