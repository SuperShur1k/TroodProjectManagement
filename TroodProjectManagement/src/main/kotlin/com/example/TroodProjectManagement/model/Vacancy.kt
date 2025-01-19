package com.example.TroodProjectManagement.model

data class Vacancy(
    val id: String? = null,
    val name: String,
    val field: String,
    val experience: String,
    val country: String,
    val description: String
)