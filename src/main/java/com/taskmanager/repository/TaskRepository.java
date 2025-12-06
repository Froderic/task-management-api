package com.taskmanager.repository;

import com.taskmanager.model.Priority;
import com.taskmanager.model.Status;
import com.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // JpaRepository provides all basic CRUD methods automatically
    // We can add custom queries later if needed
    Page<Task> findAll(Pageable pageable);

    Page<Task> findByStatus(Status status, Pageable pageable);

    Page<Task> findByPriority(Priority priority, Pageable pageable);

    Page<Task> findByStatusAndPriority(Status status, Priority priority, Pageable pageable);

    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    Page<Task> findByStatusAndPriorityAndProjectId(Status status, Priority priority, Long projectId, Pageable pageable);

    Page<Task> findByStatusAndProjectId(Status status, Long projectId, Pageable pageable);

    Page<Task> findByPriorityAndProjectId(Priority priority, Long projectId, Pageable pageable);

}