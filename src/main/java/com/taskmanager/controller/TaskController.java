package com.taskmanager.controller;

import com.taskmanager.dto.CreateTaskRequest;
import com.taskmanager.dto.UpdateTaskRequest;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Status;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // CREATE - POST /api/tasks
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request) {
        // Convert DTO to Entity
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());

        // Pass projectId to service layer
        Task createdTask = taskService.createTask(task, request.getProjectId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // READ ALL - GET /api/tasks
    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // Create Sort object
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create Pageable object
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> tasks = taskService.getAllTasks(status, priority, projectId, pageable);
        return ResponseEntity.ok(tasks);
    }

    // READ ONE - GET /api/tasks/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // UPDATE - PUT /api/tasks/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        // Convert DTO to Entity
        Task taskDetails = new Task();
        taskDetails.setTitle(request.getTitle());
        taskDetails.setDescription(request.getDescription());
        taskDetails.setStatus(request.getStatus());
        taskDetails.setPriority(request.getPriority());

        // Pass projectId to service layer
        Task task = taskService.updateTask(id, taskDetails, request.getProjectId());
        return ResponseEntity.ok(task);
    }

    // DELETE - DELETE /api/tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE ALL
    @DeleteMapping()
    public ResponseEntity<Void> deleteAllTasks() {
        taskService.deleteAllTasks();
        return ResponseEntity.noContent().build();
    }
}