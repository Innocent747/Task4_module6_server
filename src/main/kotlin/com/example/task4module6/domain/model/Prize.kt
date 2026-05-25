package com.example.task4module6.domain.model

data class Prize(
    val year: String,
    val category: String,
    val categoryFullName: String,
    val motivation: String,
    val laureates: List<Laureate>
)

data class Laureate(
    val id: String,
    val name: String,
    val portion: String,
    val motivation: String
)
