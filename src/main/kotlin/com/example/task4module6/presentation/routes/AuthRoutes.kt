package com.example.task4module6.presentation.routes

import com.example.task4module6.presentation.dto.ErrorResponse
import com.example.task4module6.presentation.dto.LoginRequest
import com.example.task4module6.presentation.dto.LoginResponse
import com.example.task4module6.security.JwtConfig
import com.example.task4module6.security.JwtTokenProvider
import com.example.task4module6.usecase.LoginUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(
    loginUseCase: LoginUseCase,
    tokenProvider: JwtTokenProvider,
    jwtConfig: JwtConfig
) {
    route("/auth") {
        post("/login") {
            val request = runCatching { call.receive<LoginRequest>() }.getOrNull()
                ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Неверный формат запроса", 400)
                )

            val isValid = loginUseCase(request.login, request.password)
            if (!isValid) {
                return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse("Неверный логин или пароль", 401)
                )
            }

            call.respond(
                LoginResponse(
                    token = tokenProvider.createToken(request.login),
                    expiresInSeconds = (jwtConfig.expiresInMillis / 1000).toInt()
                )
            )
        }
    }
}
