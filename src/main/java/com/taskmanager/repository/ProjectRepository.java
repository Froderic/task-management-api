package com.taskmanager.repository;

import com.taskmanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // JpaRepository gives us all CRUD methods for free
    // We can add custom queries here if needed later
}