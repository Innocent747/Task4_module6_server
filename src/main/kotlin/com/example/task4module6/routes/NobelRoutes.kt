package com.example.task4module6.routes

import com.example.task4module6.models.ErrorResponse
import com.example.task4module6.service.NobelService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.nobelRoutes(nobelService: NobelService) {

    route("/api/prizes") {

        // GET /api/prizes?year=2023&category=physics&limit=20&offset=0
        get {
            try {
                val year = call.request.queryParameters["year"]?.toIntOrNull()
                val category = call.request.queryParameters["category"]
                val limit = call.request.queryParameters["limit"]?.toIntOrNull()?.coerceIn(1, 100) ?: 20
                val offset = call.request.queryParameters["offset"]?.toIntOrNull()?.coerceAtLeast(0) ?: 0

                val prizes = nobelService.getPrizes(year, category, limit, offset)
                call.respond(mapOf("prizes" to prizes, "count" to prizes.size))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse("Ошибка получения данных: ${e.message}", 500)
                )
            }
        }

        // GET /api/prizes/{year}/{category}
        get("/{year}/{category}") {
            try {
                val year = call.parameters["year"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest, ErrorResponse("Укажите год", 400)
                )
                val category = call.parameters["category"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest, ErrorResponse("Укажите категорию", 400)
                )
                val prize = nobelService.getPrizeByYearAndCategory(year, category)
                if (prize != null) {
                    call.respond(prize)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse("Премия не найдена: $year / $category", 404)
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse("Ошибка: ${e.message}", 500)
                )
            }
        }
    }

    route("/api/categories") {
        get {
            try {
                val categories = nobelService.getCategories()
                call.respond(mapOf("categories" to categories))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse("Ошибка: ${e.message}", 500)
                )
            }
        }
    }

    route("/api/years") {
        get {
            try {
                val years = nobelService.getYears()
                call.respond(mapOf("years" to years))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse("Ошибка: ${e.message}", 500)
                )
            }
        }
    }
}
