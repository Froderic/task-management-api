package com.taskmanager.service;

import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Project;
import com.taskmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Create
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    // Read All (with pagination)
    public Page<Project> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    // Read One
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Project not found with id: " + id));
    }

    // Update
    public Project updateProject(Long id, Project projectDetails) {
        Project project = getProjectById(id);

        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());

        return projectRepository.save(project);
    }

    // Delete
    public void deleteProject(Long id) {
        Project project = getProjectById(id);
        projectRepository.delete(project);
    }
}