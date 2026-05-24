package com.example.task4module6

import com.example.task4module6.models.NobelPrizesResponse
import com.example.task4module6.routes.nobelRoutes
import com.example.task4module6.service.NobelService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
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
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; isLenient = true })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    val nobelService = NobelService(httpClient)

    install(ContentNegotiation) {
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
            call.respond(mapOf(
                "status" to "ok",
                "service" to "Nobel Prize API",
                "version" to "1.0",
                "endpoints" to listOf(
                    "GET /api/prizes",
                    "GET /api/prizes?year=2023&category=physics&limit=20&offset=0",
                    "GET /api/prizes/{year}/{category}",
                    "GET /api/categories",
                    "GET /api/years"
                )
            ))
        }

        nobelRoutes(nobelService)
    }
}
