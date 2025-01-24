package com.example.TroodProjectManagement.service

import com.example.TroodProjectManagement.model.Project
import com.example.TroodProjectManagement.repository.ProjectRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

/**
 * Service class for handling project-related business logic.
 */
@Service
class ProjectService(private val projectRepository: ProjectRepository) {

    /**
     * Retrieves all projects.
     *
     * @return A list of projects or a no-content response if none exist.
     */
    fun getAllProjects(): CompletableFuture<ResponseEntity<List<Project>>> {
        return projectRepository.getAllProjects().thenApply { projects ->
            if (projects.isEmpty()) {
                ResponseEntity.noContent().build()
            } else {
                ResponseEntity.ok(projects)
            }
        }
    }

    /**
     * Retrieves a project by its ID.
     *
     * @param id The project ID.
     * @return The project details or a not-found response.
     */
    fun getProjectById(id: String): CompletableFuture<ResponseEntity<String>> {
        return projectRepository.getProjectById(id).thenApply { project ->
            if (project == null) {
                ResponseEntity.status(404).body("Project with ID: $id not found")
            } else {
                ResponseEntity.ok(project.toString())
            }
        }
    }

    /**
     * Creates a new project.
     *
     * @param project The project to create.
     * @return A response with the created project's ID.
     */
    fun createProject(project: Project): CompletableFuture<ResponseEntity<String>> {
        return projectRepository.createProject(project).thenApply { id ->
            ResponseEntity.ok("Project created successfully with ID: $id")
        }
    }

    /**
     * Updates an existing project.
     *
     * @param id The project ID.
     * @param project The updated project details.
     * @return A success message or a not-found response.
     */
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

    /**
     * Deletes a project by its ID.
     *
     * @param id The project ID.
     * @return A success message or a not-found response.
     */
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