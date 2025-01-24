package com.example.TroodProjectManagement.service

import com.example.TroodProjectManagement.model.Vacancy
import com.example.TroodProjectManagement.repository.VacancyRepository
import com.example.TroodProjectManagement.repository.ProjectRepository
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

/**
 * Service class for handling vacancy-related business logic.
 */
@Service
class VacancyService(
    private val vacancyRepository: VacancyRepository,
    private val projectRepository: ProjectRepository
) {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    /**
     * Retrieves all vacancies for a given project ID.
     *
     * @param projectId The project ID.
     * @return A list of vacancies or an error response if the project is not found.
     */
    fun getVacanciesByProjectId(projectId: String): CompletableFuture<ResponseEntity<Any>> {
        return projectRepository.getProjectById(projectId).thenCompose { project ->
            if (project == null) {
                CompletableFuture.completedFuture(ResponseEntity.status(404).body(mapOf("error" to "Project with ID: $projectId not found")))
            } else {
                vacancyRepository.getVacanciesByProjectId(projectId)
                    .thenApply { vacancies ->
                        if (vacancies.isEmpty()) {
                            ResponseEntity.status(200).body(mapOf("message" to "No vacancies found for project ID: $projectId"))
                        } else {
                            ResponseEntity.ok(vacancies)
                        }
                    }
            }
        }
    }

    /**
     * Creates a new vacancy for a given project.
     *
     * @param projectId The project ID.
     * @param vacancy The vacancy details.
     * @return A success message or an error response if the project is not found or validation fails.
     */
    fun createVacancy(projectId: String, vacancy: Vacancy): CompletableFuture<ResponseEntity<Any>> {
        return projectRepository.getProjectById(projectId).thenCompose { project ->
            if (project == null) {
                CompletableFuture.completedFuture(ResponseEntity.status(404).body(mapOf("error" to "Project with ID: $projectId not found")))
            } else {
                val violations = validator.validate(vacancy)
                if (violations.isNotEmpty()) {
                    val errors = violations.associate { it.propertyPath.toString() to it.message }
                    CompletableFuture.completedFuture(ResponseEntity.status(400).body(errors))
                } else {
                    val vacancyWithProject = vacancy.copy(projectId = projectId)
                    vacancyRepository.createVacancy(vacancyWithProject)
                        .thenApply { id ->
                            ResponseEntity.ok(mapOf("message" to "Vacancy created successfully with ID: $id"))
                        }
                }
            }
        }
    }

    /**
     * Updates an existing vacancy.
     *
     * @param vacancyId The vacancy ID.
     * @param vacancy The updated vacancy details.
     * @return A success message or an error response if the vacancy is not found.
     */
    fun updateVacancy(vacancyId: String, vacancy: Vacancy): CompletableFuture<ResponseEntity<String>> {
        return vacancyRepository.getVacancyById(vacancyId)
            .thenCompose { existingVacancy ->
                if (existingVacancy == null) {
                    CompletableFuture.completedFuture(ResponseEntity.status(404).body("Vacancy with ID: $vacancyId not found"))
                } else {
                    vacancyRepository.updateVacancy(vacancyId, vacancy.copy(projectId = existingVacancy.projectId))
                        .thenApply { ResponseEntity.ok("Vacancy updated successfully!") }
                }
            }
    }

    /**
     * Deletes a vacancy by its ID.
     *
     * @param vacancyId The vacancy ID.
     * @return A success message or an error response if the vacancy is not found.
     */
    fun deleteVacancy(vacancyId: String): CompletableFuture<ResponseEntity<String>> {
        return vacancyRepository.getVacancyById(vacancyId)
            .thenCompose { existingVacancy ->
                if (existingVacancy == null) {
                    CompletableFuture.completedFuture(ResponseEntity.status(404).body("Vacancy with ID: $vacancyId not found"))
                } else {
                    vacancyRepository.deleteVacancy(vacancyId)
                        .thenApply { ResponseEntity.ok("Vacancy deleted successfully!") }
                }
            }
    }
}