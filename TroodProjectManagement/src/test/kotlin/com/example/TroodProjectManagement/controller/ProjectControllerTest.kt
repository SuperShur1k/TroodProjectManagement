package com.example.TroodProjectManagement.controller

import com.example.TroodProjectManagement.model.Project
import com.example.TroodProjectManagement.service.ProjectService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.beans.factory.annotation.Autowired
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.concurrent.CompletableFuture

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProjectControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
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
    fun `should create a project`() {
        val projectJson = jacksonObjectMapper().writeValueAsString(testProject)
        `when`(projectService.createProject(testProject))
            .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok("Project created successfully")))

        val result = mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(projectJson))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(content().string("Project created successfully"))
    }

    @Test
    fun `should return all projects`() {
        `when`(projectService.getAllProjects())
            .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok(listOf(testProject))))

        val result = mockMvc.perform(get("/projects"))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].name").value("Test Project"))
    }

    @Test
    fun `should return project by ID`() {
        `when`(projectService.getProjectById("test-id"))
            .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok("Project found")))

        val result = mockMvc.perform(get("/projects/test-id"))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(content().string("Project found"))
    }

    @Test
    fun `should update a project`() {
        val updatedProject = testProject.copy(name = "Updated Project")
        val projectJson = jacksonObjectMapper().writeValueAsString(updatedProject)
        `when`(projectService.updateProject("test-id", updatedProject))
            .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok("Project updated successfully")))

        val result = mockMvc.perform(put("/projects/test-id")
            .contentType(MediaType.APPLICATION_JSON)
            .content(projectJson))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(content().string("Project updated successfully"))
    }

    @Test
    fun `should delete a project`() {
        `when`(projectService.deleteProject("test-id"))
            .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok("Project deleted successfully")))

        val result = mockMvc.perform(delete("/projects/test-id"))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(content().string("Project deleted successfully"))
    }
}
