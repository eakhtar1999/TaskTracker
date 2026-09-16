package com.tasktracker.mapper;

import com.tasktracker.model.Task;
import com.tasktracker.model.TaskStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(TaskStatus.valueOf(rs.getString("status")));

        Timestamp createdAt = rs.getTimestamp("created_at");
        task.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        task.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return task;
    }
}