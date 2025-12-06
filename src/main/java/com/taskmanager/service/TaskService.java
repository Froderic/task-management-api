package com.taskmanager.service;

import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Project;
import com.taskmanager.model.Status;
import com.taskmanager.model.Task;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // Create new task - accepts projectId directly
    public Task createTask(Task task, Long projectId) {
        // If a projectId is provided, fetch and set the project
        if (projectId != null) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new TaskNotFoundException("Project not found with id: " + projectId));
            task.setProject(project);
        }
        return taskRepository.save(task);
    }

    // Get all tasks
    public Page<Task> getAllTasks(Status status, Priority priority, Long projectId, Pageable pageable) {
        // Filter by all three: status, priority, and project
        if (status != null && priority != null && projectId != null) {
            return taskRepository.findByStatusAndPriorityAndProjectId(status, priority, projectId, pageable);
        }

        // Filter by status and project
        if (status != null && projectId != null) {
            return taskRepository.findByStatusAndProjectId(status, projectId, pageable);
        }

        // Filter by priority and project
        if (priority != null && projectId != null) {
            return taskRepository.findByPriorityAndProjectId(priority, projectId, pageable);
        }

        // Filter by project only
        if (projectId != null) {
            return taskRepository.findByProjectId(projectId, pageable);
        }

        // If both status and priority are specified
        if (status != null && priority != null) {
            return taskRepository.findByStatusAndPriority(status, priority, pageable);
        }

        // If only status is specified
        if (status != null) {
            return taskRepository.findByStatus(status, pageable);
        }

        // If only priority is specified
        if (priority != null) {
            return taskRepository.findByPriority(priority, pageable);
        }

        // No filters - return all tasks
        return taskRepository.findAll(pageable);
    }

    // Get task by ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // Update existing task
    // Update existing task - accepts projectId directly
    public Task updateTask(Long id, Task taskDetails, Long projectId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());

        // Handle project assignment/update
        if (projectId != null) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new TaskNotFoundException("Project not found with id: " + projectId));
            task.setProject(project);
        } else {
            // If projectId is null, remove the assignment
            task.setProject(null);
        }

        return taskRepository.save(task);
    }

    // Delete task
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    // Delete all tasks
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }
}