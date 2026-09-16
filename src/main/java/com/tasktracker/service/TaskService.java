package com.tasktracker.service;

import com.tasktracker.dto.TaskRequest;
import com.tasktracker.dto.TaskResponse;
import com.tasktracker.model.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskResponse create(TaskRequest request);

    TaskResponse getById(Long id);

    List<TaskResponse> getAll(TaskStatus statusFilter);

    TaskResponse update(Long id, TaskRequest request);

    void delete(Long id);
}