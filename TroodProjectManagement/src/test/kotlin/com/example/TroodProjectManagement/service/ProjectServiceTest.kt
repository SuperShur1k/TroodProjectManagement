package com.example.TroodProjectManagement.service

import com.example.TroodProjectManagement.model.Project
import com.example.TroodProjectManagement.repository.ProjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.concurrent.CompletableFuture

@ExtendWith(MockitoExtension::class)
class ProjectServiceTest {

    @Mock
    private lateinit var projectRepository: ProjectRepository

    @InjectMocks
    private lateinit var projectService: ProjectService

    private val testProject = Project(
        id = "test-id",
        name = "Test Project",
        field = "IT",
        experience = "3 years",
        deadline = "2025-12-31",
        description = "Test description"
    )

    @Test
    fun `should return all projects`() {
        `when`(projectRepository.getAllProjects()).thenReturn(CompletableFuture.completedFuture(listOf(testProject)))

        val result = projectService.getAllProjects().get()
        assertEquals(1, result.body?.size)
    }

    @Test
    fun `should return project by ID`() {
        `when`(projectRepository.getProjectById("test-id")).thenReturn(CompletableFuture.completedFuture(testProject))

        val result = projectService.getProjectById("test-id").get()
        assertEquals(testProject.toString(), result.body)
    }

    @Test
    fun `should create a project`() {
        `when`(projectRepository.createProject(testProject)).thenReturn(CompletableFuture.completedFuture("test-id"))

        val result = projectService.createProject(testProject).get()
        assertEquals("Project created successfully with ID: test-id", result.body)
    }

    @Test
    fun `should update a project`() {
        `when`(projectRepository.getProjectById("test-id")).thenReturn(CompletableFuture.completedFuture(testProject))
        `when`(projectRepository.updateProject("test-id", testProject)).thenReturn(CompletableFuture.completedFuture(null))

        val result = projectService.updateProject("test-id", testProject).get()
        assertEquals("Project updated successfully!", result.body)
    }

    @Test
    fun `should delete a project`() {
        `when`(projectRepository.getProjectById("test-id")).thenReturn(CompletableFuture.completedFuture(testProject))
        `when`(projectRepository.deleteProject("test-id")).thenReturn(CompletableFuture.completedFuture(null))

        val result = projectService.deleteProject("test-id").get()
        assertEquals("Project deleted successfully!", result.body)
    }
}
