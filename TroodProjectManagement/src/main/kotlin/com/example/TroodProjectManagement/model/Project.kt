package com.example.TroodProjectManagement.model

import jakarta.validation.constraints.NotBlank

data class Project(
    val id: String? = null,

    @field:NotBlank(message = "Project name cannot be empty")
    val name: String,

    @field:NotBlank(message = "Field cannot be empty")
    val field: String,

    @field:NotBlank(message = "Experience cannot be empty")
    val experience: String,

    val deadline: String? = null,
    val description: String? = null
) {
    constructor() : this(null, "", "", "", null, null)
}