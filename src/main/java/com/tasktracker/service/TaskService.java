package com.tasktracker.service;

import com.tasktracker.dto.TaskRequest;
import com.tasktracker.dto.TaskResponse;

public interface TaskService {
    TaskResponse create(TaskRequest request);
}
