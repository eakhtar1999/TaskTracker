package com.tasktracker.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forTask(Long id) {
        return new ResourceNotFoundException("Task not found with id: " + id);
    }
}