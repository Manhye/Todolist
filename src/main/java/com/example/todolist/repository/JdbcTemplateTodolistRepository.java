package com.example.todolist.repository;

import com.example.todolist.dto.TodolistResponseDto;
import com.example.todolist.entity.Todolist;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class JdbcTemplateTodolistRepository implements TodolistRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateTodolistRepository(DataSource datasource){
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    @Override
    public TodolistResponseDto saveTodolist(Todolist todolist) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("todolist").usingGeneratedKeyColumns("id")
                .usingColumns("task", "description", "author", "password", "scheduled_date");
        // This part allows created_at and updated_at to be default value.

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", todolist.getTask());
        parameters.put("description", todolist.getDescription());
        parameters.put("author",todolist.getAuthor());
        parameters.put("password",todolist.getPassword());
        parameters.put("scheduled_date",todolist.getScheduled_date());


        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));


//        // This can be also done by SQL ways.
//        String sql = "INSERT INTO todolist (task, description, author, password, scheduled_date) " +
//                "VALUES (?, ?, ?, ?, ?)";
//
//        // insert
//        jdbcTemplate.update(sql,
//                todolist.getTask(),
//                todolist.getDescription(),
//                todolist.getAuthor(),
//                todolist.getPassword(),
//                todolist.getScheduled_date()
//        );
//
//
//        String lastInsertIdSql = "SELECT LAST_INSERT_ID()";
//
//        int id = jdbcTemplate.queryForObject(lastInsertIdSql, Integer.class);



        String selectSql = "SELECT id, task, description, author, scheduled_date, created_at, updated_at FROM todolist WHERE id = ?";


        return jdbcTemplate.queryForObject(
                selectSql,
                new Object[]{key.intValue()},
                (rs, rowNum) -> new TodolistResponseDto(
                        rs.getInt("id"),
                        rs.getString("task"),
                        rs.getString("description"),
                        rs.getString("author"),
                        rs.getDate("scheduled_date"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                )
        );

    }

    @Override
    public List<TodolistResponseDto> findAllTodolists() {
        return jdbcTemplate.query("select * from todolist", todolistRowMapper());
    }

    @Override
    public List<TodolistResponseDto> findTodolistByScheduled_date(Date scheduled_date) {
        return jdbcTemplate.query("select id, task, description, author, scheduled_date, created_at, updated_at from todolist where scheduled_date = ?",todolistRowMapper(),scheduled_date);
    }

    @Override
    public Todolist findTodolistByIdOrElseThrow(int id) {
        List<Todolist> result = jdbcTemplate.query("select * from todolist where id = ?", todolistRowMapperV2(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Does not exist id = " + id));
    }

    @Override
    public int updatedTodolist(int id, String task, String description, String author, Date scheduled_date) {
        return jdbcTemplate.update("update todolist set task = ?, description = ?, author = ?, scheduled_date = ? where id = ?",
                task, description, author, scheduled_date, id);
    }

    @Override
    public String findPasswordById(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT password FROM todolist WHERE id = ?",
                String.class,
                id
        );
    }

    @Override
    public int deleteTodolist(int id) {
        return jdbcTemplate.update("delete from todolist where id = ?", id);
    }


    private RowMapper<TodolistResponseDto> todolistRowMapper() {
        return new RowMapper<TodolistResponseDto>(){
            @Override
            public TodolistResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new TodolistResponseDto(
                        rs.getInt("id"),
                        rs.getString("task"),
                        rs.getString("description"),
                        rs.getString("author"),
                        rs.getDate("scheduled_date"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }
        };
    }

    private RowMapper<Todolist> todolistRowMapperV2() {
        return new RowMapper<Todolist>(){
            @Override
            public Todolist mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Todolist(
                        rs.getInt("id"),
                        rs.getString("task"),
                        rs.getString("description"),
                        rs.getString("author"),
                        rs.getDate("scheduled_date"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }
        };
    }
}
