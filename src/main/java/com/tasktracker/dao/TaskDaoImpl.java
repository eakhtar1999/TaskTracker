package com.tasktracker.dao;

import com.tasktracker.mapper.TaskRowMapper;
import com.tasktracker.model.Task;
import com.tasktracker.model.TaskStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskDaoImpl implements TaskDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final TaskRowMapper rowMapper = new TaskRowMapper();

    public TaskDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Task save(Task task) {
        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }

        String sql = """
                INSERT INTO tasks (title, description, status, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(task.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(task.getUpdatedAt()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        task.setId(key != null ? key.longValue() : null);
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        List<Task> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks ORDER BY id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        String sql = "SELECT * FROM tasks WHERE status = :status ORDER BY id";
        MapSqlParameterSource params = new MapSqlParameterSource("status", status.name());
        return namedJdbcTemplate.query(sql, params, rowMapper);
    }

    @Override
    public boolean update(Task task) {
        task.setUpdatedAt(LocalDateTime.now());
        String sql = """
        UPDATE tasks
        SET title = :title, description = :description, status = :status, updated_at = :updatedAt
        WHERE id = :id
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", task.getTitle())
                .addValue("description", task.getDescription())
                .addValue("status", task.getStatus().name())
                .addValue("updatedAt", Timestamp.valueOf(task.getUpdatedAt()))
                .addValue("id", task.getId());
        int rows = namedJdbcTemplate.update(sql, params);
        return rows > 0;
    }
}