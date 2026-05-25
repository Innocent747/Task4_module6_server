package com.example.task4module6.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class JwtTokenProvider(private val jwtConfig: JwtConfig) {
    fun createToken(login: String): String {
        val now = System.currentTimeMillis()
        val expiresAt = Date(now + jwtConfig.expiresInMillis)

        return JWT.create()
            .withIssuer(jwtConfig.issuer)
            .withAudience(jwtConfig.audience)
            .withSubject(login)
            .withExpiresAt(expiresAt)
            .sign(Algorithm.HMAC256(jwtConfig.secret))
    }
}
