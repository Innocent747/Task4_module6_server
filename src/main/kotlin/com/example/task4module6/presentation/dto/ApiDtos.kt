package com.example.task4module6.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
    val service: String,
    val version: String,
    val endpoints: List<String>
)

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val expiresInSeconds: Int
)

@Serializable
data class PrizesResponse(
    val prizes: List<PrizeDto>,
    val count: Int
)

@Serializable
data class PrizeDto(
    val year: String,
    val category: String,
    val categoryFullName: String,
    val motivation: String,
    val laureates: List<LaureateDto>
)

@Serializable
data class LaureateDto(
    val id: String,
    val name: String,
    val portion: String,
    val motivation: String
)

@Serializable
data class LaureatesResponse(
    val laureates: List<LaureateDto>,
    val count: Int
)

@Serializable
data class ErrorResponse(
    val error: String,
    val status: Int
)
