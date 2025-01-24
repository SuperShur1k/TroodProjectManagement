package com.example.TroodProjectManagement.model

import jakarta.validation.constraints.NotBlank

/**
 * Represents a project entity with validation constraints.
 */
data class Project(
    val id: String? = null,

    /**
     * The name of the project. Cannot be empty.
     */
    @field:NotBlank(message = "Project name cannot be empty")
    val name: String,

    /**
     * The field or industry the project belongs to. Cannot be empty.
     */
    @field:NotBlank(message = "Field cannot be empty")
    val field: String,

    /**
     * The experience level required for the project. Cannot be empty.
     */
    @field:NotBlank(message = "Experience cannot be empty")
    val experience: String,

    /**
     * Optional deadline for the project.
     */
    val deadline: String? = null,

    /**
     * Optional project description.
     */
    val description: String? = null
) {
    /**
     * Secondary constructor with default values.
     */
    constructor() : this(null, "", "", "", null, null)
}