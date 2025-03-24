package com.example.todolist.service;

import com.example.todolist.dto.DeleteListRequestDto;
import com.example.todolist.dto.FindListResponseDto;
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

        TodolistResponseDto todolistResponseDto = todolistRepository.saveTodolist(dto);

        return todolistResponseDto;

    }

    @Override
    public List<FindListResponseDto> findAllTodolists() {
        return todolistRepository.findAllTodolists();
    }

    @Override
    public List<FindListResponseDto> findTodolistByScheduled_date(Date scheduled_date) {
        return todolistRepository.findTodolistByScheduled_date(scheduled_date);
    }

    @Override
    public FindListResponseDto findTodolistById(int id) {

        return todolistRepository.findTodolistByIdOrElseThrow(id);
    }


    @Transactional
    @Override
    public FindListResponseDto updateTodolist(int id, TodolistRequestDto dto) {
        if(dto.getTask()==null||dto.getDescription()==null||dto.getPassword()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Task, description, author, and password are required");
        }

        String email = todolistRepository.findEmailById(id);
        String password = todolistRepository.findPasswordById(id);

        if (!(password.equals(dto.getPassword())&&email.equals(dto.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }

        int updatedRow = todolistRepository.updatedTodolist(id, dto.getTask(),dto.getDescription(),dto.getScheduled_date());

        if(updatedRow==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = "+ id);
        }



        return todolistRepository.findTodolistByIdOrElseThrow(id);
    }

    @Override
    public void deleteTodolist(int id, DeleteListRequestDto dto) {

        if(dto.getEmail()==null||dto.getPassword()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email and password are required");
        }

        String email = todolistRepository.findEmailById(id);
        String password = todolistRepository.findPasswordById(id);

        if (!(password.equals(dto.getPassword())&&email.equals(dto.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }

        int deletedRow = todolistRepository.deleteTodolist(id, dto);

        if(deletedRow==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = "+ id);
        }
    }

    @Override
    public List<FindListResponseDto> findTodolistByPage(int page, int size) {
        return todolistRepository.findTodolistByPage(page, size);
    }
}
