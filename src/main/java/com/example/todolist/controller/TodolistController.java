package com.example.todolist.controller;


import com.example.todolist.dto.DeleteListRequestDto;
import com.example.todolist.dto.FindListResponseDto;
import com.example.todolist.dto.TodolistRequestDto;
import com.example.todolist.dto.TodolistResponseDto;
import com.example.todolist.service.TodolistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/todolist")
public class TodolistController {

    private final TodolistService todolistService;

    public TodolistController(TodolistService todolistService){
        this.todolistService = todolistService;
    }

    @PostMapping
    public ResponseEntity<TodolistResponseDto> saveTodolist(@Valid @RequestBody TodolistRequestDto dto){
        return new ResponseEntity<>(todolistService.saveTodolist(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FindListResponseDto>> findAllTodolists(
            @RequestParam(value = "page", defaultValue = "1000") int page,
            @RequestParam(value = "size", defaultValue = "1000") int size) {

        // exception
        if (page <= 0 || size <= 0) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<FindListResponseDto> todolist;

        if (page == 1000 && size == 1000) {
            // no paging if default
            todolist = todolistService.findAllTodolists();
        } else {
            todolist = todolistService.findTodolistByPage(page, size);
        }

        return new ResponseEntity<>(todolist, HttpStatus.OK);
    }

    @GetMapping("/scheduled_date")
    public ResponseEntity<List<FindListResponseDto>> findTodolistByScheduled_date(@RequestParam("scheduled_date") Date scheduled_date){
        List<FindListResponseDto> todolist = todolistService.findTodolistByScheduled_date(scheduled_date);

        return new ResponseEntity<>(todolist,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindListResponseDto> findTodolistById(@PathVariable int id){
        return new ResponseEntity<>(todolistService.findTodolistById(id),HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<FindListResponseDto>> findTodolistByUserId(@RequestParam("id") int id){
        List<FindListResponseDto> todolist = todolistService.findTodolistByUserId(id);
        return new ResponseEntity<>(todolist,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FindListResponseDto> updateTodolist(@PathVariable int id, @Valid @RequestBody TodolistRequestDto dto){
        return new ResponseEntity<>(todolistService.updateTodolist(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodolist(@PathVariable int id, @RequestBody DeleteListRequestDto dto){
        todolistService.deleteTodolist(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

