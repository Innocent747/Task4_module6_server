package com.example.task4module6.presentation

import com.example.task4module6.presentation.dto.HealthResponse
import com.example.task4module6.presentation.routes.authRoutes
import com.example.task4module6.presentation.routes.prizeRoutes
import com.example.task4module6.security.JwtConfig
import com.example.task4module6.security.JwtTokenProvider
import com.example.task4module6.usecase.GetPrizeDetailsUseCase
import com.example.task4module6.usecase.GetPrizeLaureatesUseCase
import com.example.task4module6.usecase.GetPrizesUseCase
import com.example.task4module6.usecase.LoginUseCase
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Application.configureRouting(
    getPrizesUseCase: GetPrizesUseCase,
    getPrizeDetailsUseCase: GetPrizeDetailsUseCase,
    getPrizeLaureatesUseCase: GetPrizeLaureatesUseCase,
    loginUseCase: LoginUseCase,
    tokenProvider: JwtTokenProvider,
    jwtConfig: JwtConfig
) {
    routing {
        get("/") {
            call.respond(
                HealthResponse(
                    status = "ok",
                    service = "Nobel Prize API",
                    version = "1.0",
                    endpoints = listOf(
                        "POST /auth/login",
                        "GET /prizes",
                        "GET /prizes/{year}/{category}",
                        "GET /prizes/{year}/{category}/laureates"
                    )
                )
            )
        }

        authRoutes(
            loginUseCase = loginUseCase,
            tokenProvider = tokenProvider,
            jwtConfig = jwtConfig
        )

        prizeRoutes(
            getPrizesUseCase = getPrizesUseCase,
            getPrizeDetailsUseCase = getPrizeDetailsUseCase,
            getPrizeLaureatesUseCase = getPrizeLaureatesUseCase
        )
    }
}
