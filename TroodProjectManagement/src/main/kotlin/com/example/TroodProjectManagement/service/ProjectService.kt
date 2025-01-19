package com.example.TroodProjectManagement.service

import com.example.TroodProjectManagement.model.Project
import com.example.troodprojectmanagement.repository.ProjectRepository
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class ProjectService(private val projectRepository: ProjectRepository) {

    fun getAllProjects(): CompletableFuture<List<Project>> {
        return projectRepository.getAllProjects()
    }

    fun getProjectById(id: String): CompletableFuture<Project?> {
        return projectRepository.getProjectById(id)
    }

    fun createProject(project: Project): CompletableFuture<String> {
        return projectRepository.createProject(project)
    }

    fun updateProject(id: String, project: Project): CompletableFuture<Void> {
        return projectRepository.updateProject(id, project)
    }

    fun deleteProject(id: String): CompletableFuture<Void> {
        return projectRepository.deleteProject(id)
    }
}