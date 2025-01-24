package com.example.TroodProjectManagement.controller

import com.example.TroodProjectManagement.model.Project
import com.example.TroodProjectManagement.service.ProjectService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

/**
 * Controller for handling project-related requests.
 */
@RestController
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) {
    private val logger = LoggerFactory.getLogger(ProjectController::class.java)

    /**
     * Retrieves all projects.
     *
     * @return A list of projects.
     */
    @GetMapping
    fun getAllProjects(): CompletableFuture<ResponseEntity<List<Project>>> {
        return projectService.getAllProjects()
    }

    /**
     * Retrieves a project by its ID.
     *
     * @param id Project ID.
     * @return The requested project as a string.
     */
    @GetMapping("/{id}")
    fun getProjectById(@PathVariable id: String): CompletableFuture<ResponseEntity<String>> {
        return projectService.getProjectById(id)
    }

    /**
     * Creates a new project.
     *
     * @param project The project to create.
     * @return A success message.
     */
    @PostMapping
    fun createProject(@Valid @RequestBody project: Project): CompletableFuture<ResponseEntity<String>> {
        logger.info("Creating project: $project")
        return projectService.createProject(project)
    }

    /**
     * Updates an existing project.
     *
     * @param id Project ID.
     * @param project The updated project data.
     * @return A success message.
     */
    @PutMapping("/{id}")
    fun updateProject(@PathVariable id: String, @Valid @RequestBody project: Project): CompletableFuture<ResponseEntity<String>> {
        return projectService.updateProject(id, project)
    }

    /**
     * Deletes a project by its ID.
     *
     * @param id Project ID.
     * @return A success message.
     */
    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: String): CompletableFuture<ResponseEntity<String>> {
        return projectService.deleteProject(id)
    }
}