package com.example.TroodProjectManagement.service

import com.example.TroodProjectManagement.model.Vacancy
import com.example.TroodProjectManagement.model.Project
import com.example.TroodProjectManagement.repository.VacancyRepository
import com.example.TroodProjectManagement.repository.ProjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.concurrent.CompletableFuture

@ExtendWith(MockitoExtension::class)
class VacancyServiceTest {

    @Mock
    private lateinit var vacancyRepository: VacancyRepository

    @Mock
    private lateinit var projectRepository: ProjectRepository

    @InjectMocks
    private lateinit var vacancyService: VacancyService

    private val testProject = Project(
        id = "project-id",
        name = "Test Project",
        field = "IT",
        experience = "3 years",
        deadline = "2025-12-31",
        description = "Test project description"
    )

    private val testVacancy = Vacancy(
        id = "vacancy-id",
        name = "Test Vacancy",
        field = "IT",
        experience = "3 years",
        deadline = "2025-12-31",
        description = "Test description",
        projectId = "project-id"
    )

    @Test
    fun `should return vacancies by project ID`() {
        `when`(projectRepository.getProjectById("project-id")).thenReturn(CompletableFuture.completedFuture(testProject))
        `when`(vacancyRepository.getVacanciesByProjectId("project-id")).thenReturn(CompletableFuture.completedFuture(listOf(testVacancy)))

        val result = vacancyService.getVacanciesByProjectId("project-id").get()
        assertEquals(200, result.statusCodeValue)
        assertEquals(1, (result.body as List<*>).size)
    }

    @Test
    fun `should return error if project does not exist`() {
        `when`(projectRepository.getProjectById("invalid-id")).thenReturn(CompletableFuture.completedFuture(null))

        val result = vacancyService.getVacanciesByProjectId("invalid-id").get()
        assertEquals(404, result.statusCodeValue)
    }

    @Test
    fun `should create a vacancy`() {
        `when`(projectRepository.getProjectById("project-id")).thenReturn(CompletableFuture.completedFuture(testProject))
        `when`(vacancyRepository.createVacancy(testVacancy)).thenReturn(CompletableFuture.completedFuture("vacancy-id"))

        val result = vacancyService.createVacancy("project-id", testVacancy).get()
        assertEquals(200, result.statusCodeValue)
        assertEquals("Vacancy created successfully with ID: vacancy-id", (result.body as Map<*, *>) ["message"])
    }

    @Test
    fun `should return error if vacancy validation fails`() {
        val invalidVacancy = testVacancy.copy(name = "")
        `when`(projectRepository.getProjectById("project-id")).thenReturn(CompletableFuture.completedFuture(testProject))

        val result = vacancyService.createVacancy("project-id", invalidVacancy).get()
        assertEquals(400, result.statusCodeValue)
    }

    @Test
    fun `should update a vacancy`() {
        `when`(vacancyRepository.getVacancyById("vacancy-id")).thenReturn(CompletableFuture.completedFuture(testVacancy))
        `when`(vacancyRepository.updateVacancy("vacancy-id", testVacancy)).thenReturn(CompletableFuture.completedFuture(null))

        val result = vacancyService.updateVacancy("vacancy-id", testVacancy).get()
        assertEquals(200, result.statusCodeValue)
        assertEquals("Vacancy updated successfully!", result.body)
    }

    @Test
    fun `should return error if vacancy not found for update`() {
        `when`(vacancyRepository.getVacancyById("invalid-id")).thenReturn(CompletableFuture.completedFuture(null))

        val result = vacancyService.updateVacancy("invalid-id", testVacancy).get()
        assertEquals(404, result.statusCodeValue)
    }

    @Test
    fun `should delete a vacancy`() {
        `when`(vacancyRepository.getVacancyById("vacancy-id")).thenReturn(CompletableFuture.completedFuture(testVacancy))
        `when`(vacancyRepository.deleteVacancy("vacancy-id")).thenReturn(CompletableFuture.completedFuture(null))

        val result = vacancyService.deleteVacancy("vacancy-id").get()
        assertEquals(200, result.statusCodeValue)
        assertEquals("Vacancy deleted successfully!", result.body)
    }

    @Test
    fun `should return error if vacancy not found for delete`() {
        `when`(vacancyRepository.getVacancyById("invalid-id")).thenReturn(CompletableFuture.completedFuture(null))

        val result = vacancyService.deleteVacancy("invalid-id").get()
        assertEquals(404, result.statusCodeValue)
    }
}
