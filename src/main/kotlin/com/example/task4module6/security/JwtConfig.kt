package com.example.task4module6.security

import kotlin.time.Duration.Companion.minutes

data class JwtConfig(
    val issuer: String = "task4-module6-server",
    val audience: String = "task4-module6-client",
    val realm: String = "Task4 API",
    val secret: String = "task4_module6_server_secret_key_32chars_min",
    val expiresInMillis: Long = 30.minutes.inWholeMilliseconds
)
