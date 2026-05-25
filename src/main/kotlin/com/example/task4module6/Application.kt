package com.example.task4module6

import com.example.task4module6.data.repository.FilePrizeRepository
import com.example.task4module6.data.repository.InMemoryUserRepository
import com.example.task4module6.data.source.ResourcePrizeDataSource
import com.example.task4module6.plugins.configureCallLogging
import com.example.task4module6.plugins.configureSecurity
import com.example.task4module6.plugins.configureSerialization
import com.example.task4module6.presentation.configureRouting
import com.example.task4module6.security.JwtConfig
import com.example.task4module6.security.JwtTokenProvider
import com.example.task4module6.usecase.GetPrizeDetailsUseCase
import com.example.task4module6.usecase.GetPrizeLaureatesUseCase
import com.example.task4module6.usecase.GetPrizesUseCase
import com.example.task4module6.usecase.LoginUseCase
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val jwtConfig = JwtConfig()
    val tokenProvider = JwtTokenProvider(jwtConfig)

    val prizeRepository = FilePrizeRepository(ResourcePrizeDataSource())
    val userRepository = InMemoryUserRepository()

    val getPrizesUseCase = GetPrizesUseCase(prizeRepository)
    val getPrizeDetailsUseCase = GetPrizeDetailsUseCase(prizeRepository)
    val getPrizeLaureatesUseCase = GetPrizeLaureatesUseCase(prizeRepository)
    val loginUseCase = LoginUseCase(userRepository)

    configureSerialization()
    configureCallLogging()
    configureSecurity(jwtConfig)
    configureRouting(
        getPrizesUseCase = getPrizesUseCase,
        getPrizeDetailsUseCase = getPrizeDetailsUseCase,
        getPrizeLaureatesUseCase = getPrizeLaureatesUseCase,
        loginUseCase = loginUseCase,
        tokenProvider = tokenProvider,
        jwtConfig = jwtConfig
    )
}
