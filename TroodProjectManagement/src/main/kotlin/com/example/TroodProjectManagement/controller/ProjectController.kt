package com.example.TroodProjectManagement.controller

import com.example.TroodProjectManagement.model.Project
import com.example.TroodProjectManagement.service.ProjectService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) {

    @GetMapping
    fun getAllProjects(): CompletableFuture<ResponseEntity<List<Project>>> {
        return projectService.getAllProjects()
    }

    @GetMapping("/{id}")
    fun getProjectById(@PathVariable id: String): CompletableFuture<ResponseEntity<String>> {
        return projectService.getProjectById(id)
    }

    @PostMapping
    fun createProject(@Valid @RequestBody project: Project): CompletableFuture<ResponseEntity<String>> {
        return projectService.createProject(project)
    }

    @PutMapping("/{id}")
    fun updateProject(@PathVariable id: String, @Valid @RequestBody project: Project): CompletableFuture<ResponseEntity<String>> {
        return projectService.updateProject(id, project)
    }

    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: String): CompletableFuture<ResponseEntity<String>> {
        return projectService.deleteProject(id)
    }
}