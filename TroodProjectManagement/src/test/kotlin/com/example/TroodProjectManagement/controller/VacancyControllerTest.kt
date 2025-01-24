package com.example.TroodProjectManagement.controller

import com.example.TroodProjectManagement.model.Vacancy
import com.example.TroodProjectManagement.service.VacancyService
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
class VacancyControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var vacancyService: VacancyService

    private val objectMapper = jacksonObjectMapper()

    private val testVacancy = Vacancy(
        id = "1",
        name = "Software Engineer",
        field = "IT",
        experience = "3 years",
        deadline = "2025-12-31",
        description = "Great job",
        projectId = "123"
    )

    @Test
    fun `should return vacancies for a given project`() {
        `when`(vacancyService.getVacanciesByProjectId("123"))
            .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok(listOf(testVacancy))))

        val result = mockMvc.perform(get("/projects/123/vacancies"))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].name").value("Software Engineer"))
    }

    @Test
    fun `should return empty list when no vacancies found`() {
        `when`(vacancyService.getVacanciesByProjectId("123"))
            .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok(emptyList<Vacancy>())))

        val result = mockMvc.perform(get("/projects/123/vacancies"))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(content().json("[]"))
    }

    @Test
    fun `should create a new vacancy`() {
        val vacancyJson = objectMapper.writeValueAsString(testVacancy)
        `when`(vacancyService.createVacancy("123", testVacancy))
            .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok("Vacancy created successfully")))

        val result = mockMvc.perform(post("/projects/123/vacancies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(vacancyJson))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(content().string("Vacancy created successfully"))
    }

    @Test
    fun `should update a vacancy`() {
        val updatedVacancy = testVacancy.copy(name = "Senior Engineer")
        val vacancyJson = objectMapper.writeValueAsString(updatedVacancy)
        `when`(vacancyService.updateVacancy("1", updatedVacancy))
            .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok("Vacancy updated successfully")))

        val result = mockMvc.perform(put("/vacancies/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(vacancyJson))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(content().string("Vacancy updated successfully"))
    }

    @Test
    fun `should delete a vacancy`() {
        `when`(vacancyService.deleteVacancy("1"))
            .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok("Vacancy deleted successfully")))

        val result = mockMvc.perform(delete("/vacancies/1"))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(content().string("Vacancy deleted successfully"))
    }
}
