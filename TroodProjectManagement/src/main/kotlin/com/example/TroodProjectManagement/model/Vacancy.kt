package com.example.TroodProjectManagement.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * Represents a job vacancy with validation constraints.
 */
data class Vacancy(
    val id: String? = null,

    /**
     * The name of the vacancy. Cannot be empty and must not exceed 100 characters.
     */
    @field:NotBlank(message = "Vacancy name cannot be empty")
    @field:Size(max = 100, message = "Vacancy name must be at most 100 characters")
    val name: String,

    /**
     * The field or industry related to the vacancy. Cannot be empty.
     */
    @field:NotBlank(message = "Field cannot be empty")
    val field: String,

    /**
     * The experience required for the vacancy. Cannot be empty.
     */
    @field:NotBlank(message = "Experience cannot be empty")
    val experience: String,

    /**
     * The country required for the vacancy. Cannot be empty.
     */
    @field:NotBlank(message = "Country cannot be empty")
    val country: String,

    /**
     * The description of the vacancy. Cannot be empty and must not exceed 1000 characters.
     */
    @field:NotBlank(message = "Description cannot be empty")
    @field:Size(max = 1000, message = "Description must be at most 1000 characters")
    val description: String,

    /**
     * The ID of the project this vacancy is associated with. Optional.
     */
    val projectId: String? = null
) {
    /**
     * Secondary constructor with default values.
     */
    constructor() : this(null, "", "", "", "", "", null)
}