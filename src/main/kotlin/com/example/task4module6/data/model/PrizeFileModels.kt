package com.example.task4module6.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PrizeFile(
    val prizes: List<PrizeRecord> = emptyList()
)

@Serializable
data class PrizeRecord(
    val year: String,
    val category: String,
    val categoryFullName: String,
    val motivation: String,
    val laureates: List<LaureateRecord> = emptyList()
)

@Serializable
data class LaureateRecord(
    val id: String,
    val name: String,
    val portion: String,
    val motivation: String
)
