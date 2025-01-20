package com.example.TroodProjectManagement.service

import com.example.TroodProjectManagement.model.Project
import com.example.TroodProjectManagement.repository.ProjectRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class ProjectService(private val projectRepository: ProjectRepository) {

    fun getAllProjects(): CompletableFuture<ResponseEntity<List<Project>>> {
        return projectRepository.getAllProjects().thenApply { projects ->
            if (projects.isEmpty()) {
                ResponseEntity.noContent().build()
            } else {
                ResponseEntity.ok(projects)
            }
        }
    }

    fun getProjectById(id: String): CompletableFuture<ResponseEntity<String>> {
        return projectRepository.getProjectById(id).thenApply { project ->
            if (project == null) {
                ResponseEntity.status(404).body("Project with ID: $id not found")
            } else {
                ResponseEntity.ok(project.toString())
            }
        }
    }

    fun createProject(project: Project): CompletableFuture<ResponseEntity<String>> {
        return projectRepository.createProject(project).thenApply { id ->
            ResponseEntity.ok("Project created successfully with ID: $id")
        }
    }

    fun updateProject(id: String, project: Project): CompletableFuture<ResponseEntity<String>> {
        return projectRepository.getProjectById(id).thenCompose { existingProject ->
            if (existingProject == null) {
                CompletableFuture.completedFuture(ResponseEntity.status(404).body("Project with ID: $id not found"))
            } else {
                projectRepository.updateProject(id, project).thenApply {
                    ResponseEntity.ok("Project updated successfully!")
                }
            }
        }
    }

    fun deleteProject(id: String): CompletableFuture<ResponseEntity<String>> {
        return projectRepository.getProjectById(id).thenCompose { existingProject ->
            if (existingProject == null) {
                CompletableFuture.completedFuture(ResponseEntity.status(404).body("Project with ID: $id not found"))
            } else {
                projectRepository.deleteProject(id).thenApply {
                    ResponseEntity.ok("Project deleted successfully!")
                }
            }
        }
    }
}