package com.tasktracker.service;

import com.tasktracker.dao.TaskDao;
import com.tasktracker.dto.TaskRequest;
import com.tasktracker.dto.TaskResponse;
import com.tasktracker.exception.ResourceNotFoundException;
import com.tasktracker.model.Task;
import com.tasktracker.model.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;

    public TaskServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public TaskResponse create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING);

        Task saved = taskDao.save(task);
        return TaskResponse.from(saved);
    }

    @Override
    public TaskResponse getById(Long id) {
        Task task = taskDao.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forTask(id));
        return TaskResponse.from(task);
    }

    @Override
    public List<TaskResponse> getAll(TaskStatus statusFilter) {
        List<Task> tasks = statusFilter != null
                ? taskDao.findByStatus(statusFilter)
                : taskDao.findAll();
        return tasks.stream().map(TaskResponse::from).toList();
    }

    @Override
    public TaskResponse update(Long id, TaskRequest request) {
        Task existing = taskDao.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forTask(id));

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        taskDao.update(existing);
        return TaskResponse.from(existing);
    }

    @Override
    public void delete(Long id) {
        boolean deleted = taskDao.deleteById(id);
        if (!deleted) {
            throw ResourceNotFoundException.forTask(id);
        }
    }
}