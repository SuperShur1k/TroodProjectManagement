package com.example.TroodProjectManagement.model

data class Project(
    val id: String? = null,
    val name: String,
    val field: String,
    val experience: String,
    val deadline: String,
    val description: String
)