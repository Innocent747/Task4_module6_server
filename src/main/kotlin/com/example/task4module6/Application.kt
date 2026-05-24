package com.example.task4module6

import com.example.task4module6.routes.nobelRoutes
import com.example.task4module6.service.NobelService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.timeout.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

@Serializable
private data class HealthResponse(
    val status: String,
    val service: String,
    val version: String,
    val endpoints: List<String>
)

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // IMPORTANT: avoid name clash between client ContentNegotiation and server ContentNegotiation
    val httpClient = HttpClient(CIO) {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; isLenient = true })
        }

        install(HttpTimeout) {
            // Nobel API иногда отвечает не мгновенно. Дадим запас.
            connectTimeoutMillis = 10_000
            requestTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }

        install(Logging) {
            level = LogLevel.INFO
        }
    }

    val nobelService = NobelService(httpClient)

    install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
        json(Json { prettyPrint = true; ignoreUnknownKeys = true })
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
    }

    install(CallLogging) {
        level = Level.INFO
    }

    routing {
        // Health check
        get("/") {
            call.respond(
                HealthResponse(
                    status = "ok",
                    service = "Nobel Prize API",
                    version = "1.0",
                    endpoints = listOf(
                        "GET /api/prizes",
                        "GET /api/prizes?year=2023&category=physics&limit=20&offset=0",
                        "GET /api/prizes/{year}/{category}",
                        "GET /api/categories",
                        "GET /api/years"
                    )
                )
            )
        }

        nobelRoutes(nobelService)
    }
}
