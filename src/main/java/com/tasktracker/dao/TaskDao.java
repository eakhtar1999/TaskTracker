package com.tasktracker.dao;

import com.tasktracker.model.Task;
import com.tasktracker.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskDao {

    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAll();

    List<Task> findByStatus(TaskStatus status);

    boolean update(Task task);
}
