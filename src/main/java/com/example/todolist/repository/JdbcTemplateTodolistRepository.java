package com.example.todolist.repository;

import com.example.todolist.dto.*;
import com.example.todolist.exception.TodolistNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
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
    public TodolistResponseDto saveTodolist(TodolistRequestDto dto) {

        // 1️⃣ find name from author table of which email and password matches.
        String findAuthorSql = "SELECT id FROM author WHERE email = ? AND password = ?";
        Integer authorId = jdbcTemplate.query(findAuthorSql,
                new Object[]{dto.getEmail(), dto.getPassword()},
                (rs) -> rs.next() ? rs.getInt("id") : null);

        // 2️⃣ If it doesn't exist, add new author on the table.
        if (authorId == null) {
            SimpleJdbcInsert authorInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("author")
                    .usingGeneratedKeyColumns("id")
                    .usingColumns("name", "email", "password");

            Map<String, Object> authorParams = new HashMap<>();
            authorParams.put("name", dto.getName());
            authorParams.put("email", dto.getEmail());
            authorParams.put("password", dto.getPassword());

            Number newAuthorId = authorInsert.executeAndReturnKey(new MapSqlParameterSource(authorParams));
            authorId = newAuthorId.intValue();
        }

        // 3️⃣ add data on  todolist
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("todolist")
                .usingGeneratedKeyColumns("id")
                .usingColumns("task", "description", "author_id", "scheduled_date");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", dto.getTask());
        parameters.put("description", dto.getDescription());
        parameters.put("author_id", authorId);
        parameters.put("scheduled_date", dto.getScheduled_date());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        // 4️⃣ return added data.
        String selectSql = "SELECT id, task, description, scheduled_date, created_at, updated_at FROM todolist WHERE id = ?";
        return jdbcTemplate.queryForObject(
                selectSql,
                new Object[]{key.intValue()},
                (rs, rowNum) -> new TodolistResponseDto(
                        rs.getInt("id"),
                        rs.getString("task"),
                        rs.getString("description"),
                        rs.getDate("scheduled_date"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                )
        );
    }


    @Override
    public List<FindListResponseDto> findAllTodolists() {
        String sql = "SELECT t.id, t.task, t.description, t.author_id, t.scheduled_date, t.created_at, t.updated_at, " +
                "a.name AS name, a.email AS email " +
                "FROM todolist t " +
                "JOIN author a ON t.author_id = a.id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new FindListResponseDto(
                rs.getInt("id"),
                rs.getString("task"),
                rs.getString("description"),
                rs.getDate("scheduled_date"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getString("name"),  // Author's name
                rs.getString("email")  // Author's email
        ));
    }

    @Override
    public List<FindListResponseDto> findTodolistByScheduled_date(Date scheduled_date) {

        String sql = "SELECT t.id, t.task, t.description, t.author_id, t.scheduled_date, t.created_at, t.updated_at, " +
                "a.name AS name, a.email AS email " +
                "FROM todolist t " +
                "JOIN author a ON t.author_id = a.id " +
                "WHERE t.scheduled_date = ?";

        return jdbcTemplate.query(sql, new Object[]{scheduled_date},(rs, rowNum) -> new FindListResponseDto(
                rs.getInt("id"),
                rs.getString("task"),
                rs.getString("description"),
                rs.getDate("scheduled_date"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getString("name"),  // Author's name
                rs.getString("email")  // Author's email
        ));
    }

    @Override
    public FindListResponseDto findTodolistByIdOrElseThrow(int id) {
        String sql = "SELECT t.id, t.task, t.description, t.author_id, t.scheduled_date, t.created_at, t.updated_at, " +
                "a.name AS name, a.email AS email " +
                "FROM todolist t " +
                "JOIN author a ON t.author_id = a.id " +
                "WHERE t.id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new FindListResponseDto(
                    rs.getInt("id"),
                    rs.getString("task"),
                    rs.getString("description"),
                    rs.getDate("scheduled_date"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at"),
                    rs.getString("name"),  // Author's name
                    rs.getString("email")  // Author's email
            ));
        } catch (EmptyResultDataAccessException e) {
            throw new TodolistNotFoundException("ID does not exist. ID: " + id);
        }
    }

    @Override
    public int updatedTodolist(int id, String task, String description, Date scheduled_date) {
        return jdbcTemplate.update("update todolist set task = ?, description = ?, scheduled_date = ? where id = ?",
                task, description, scheduled_date, id);
    }


    @Override
    public int deleteTodolist(int id, DeleteListRequestDto dto) {
        return jdbcTemplate.update("delete from todolist where id = ?", id);
    }

    @Override
    public List<FindListResponseDto> findTodolistByPage(int page, int size) {
        int offset = (page - 1) * size;

        String sql = "SELECT t.id, t.task, t.description, t.scheduled_date, t.created_at, t.updated_at, " +
                "a.name AS name, a.email AS email " +
                "FROM todolist t " +
                "JOIN author a ON t.author_id = a.id " +
                "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(sql,
                new Object[]{size, offset},
                (rs, rowNum) -> new FindListResponseDto(
                        rs.getInt("id"),
                        rs.getString("task"),
                        rs.getString("description"),
                        rs.getDate("scheduled_date"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getString("name"),
                        rs.getString("email")
                )
        );
    }

    @Override
    public AuthorInfoDto findEmailAndPasswordById(int id) {
        String sql = "SELECT a.email, a.password " +
                "FROM author a "+
                "JOIN todolist t ON t.author_id = a.id " +
                "WHERE t.id = ?";

        return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new AuthorInfoDto(
                        rs.getString("email"),
                        rs.getString("password")
                ),id
        );
    }

    @Override
    public List<FindListResponseDto> findTodolistByUserId(int id) {
        String sql = "SELECT t.id, t.task, t.description, t.author_id, t.scheduled_date, t.created_at, t.updated_at, " +
                "a.name AS name, a.email AS email " +
                "FROM todolist t " +
                "JOIN author a ON t.author_id = a.id " +
                "WHERE a.id = ?";

        return jdbcTemplate.query(sql, new Object[]{id},(rs, rowNum) -> new FindListResponseDto(
                rs.getInt("id"),
                rs.getString("task"),
                rs.getString("description"),
                rs.getDate("scheduled_date"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getString("name"),  // Author's name
                rs.getString("email")  // Author's email
        ));
    }

}
