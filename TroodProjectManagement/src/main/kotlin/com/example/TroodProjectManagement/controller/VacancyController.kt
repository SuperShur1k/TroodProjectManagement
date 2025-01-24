package com.example.TroodProjectManagement.controller

import com.example.TroodProjectManagement.model.Vacancy
import com.example.TroodProjectManagement.service.VacancyService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

/**
 * Controller for managing vacancies.
 */
@RestController
@RequestMapping
class VacancyController(private val vacancyService: VacancyService) {
    private val logger = LoggerFactory.getLogger(VacancyController::class.java)

    /**
     * Retrieves all vacancies for a given project.
     *
     * @param projectId The project ID.
     * @return A list of vacancies or an error message.
     */
    @GetMapping("/projects/{projectId}/vacancies")
    fun getVacanciesByProject(@PathVariable projectId: String): CompletableFuture<ResponseEntity<Any>> {
        return vacancyService.getVacanciesByProjectId(projectId)
    }

    /**
     * Adds a new vacancy to a project.
     *
     * @param projectId The project ID.
     * @param vacancy The vacancy to add.
     * @return A success message or an error message.
     */
    @PostMapping("/projects/{projectId}/vacancies")
    fun addVacancy(
        @PathVariable projectId: String,
        @Valid @RequestBody vacancy: Vacancy
    ): CompletableFuture<ResponseEntity<Any>> {
        logger.info("Creating vacancy for project $projectId: $vacancy")
        return vacancyService.createVacancy(projectId, vacancy)
    }

    /**
     * Updates an existing vacancy.
     *
     * @param vacancyId The vacancy ID.
     * @param vacancy The updated vacancy data.
     * @return A success message or an error message.
     */
    @PutMapping("/vacancies/{vacancyId}")
    fun updateVacancy(
        @PathVariable vacancyId: String,
        @Valid @RequestBody vacancy: Vacancy
    ): CompletableFuture<ResponseEntity<String>> {
        return vacancyService.updateVacancy(vacancyId, vacancy)
    }

    /**
     * Deletes a vacancy by its ID.
     *
     * @param vacancyId The vacancy ID.
     * @return A success message or an error message.
     */
    @DeleteMapping("/vacancies/{vacancyId}")
    fun deleteVacancy(@PathVariable vacancyId: String): CompletableFuture<ResponseEntity<String>> {
        return vacancyService.deleteVacancy(vacancyId)
    }
}