package com.example.task4module6.presentation.routes

import com.example.task4module6.domain.model.Laureate
import com.example.task4module6.domain.model.Prize
import com.example.task4module6.presentation.dto.ErrorResponse
import com.example.task4module6.presentation.dto.LaureateDto
import com.example.task4module6.presentation.dto.LaureatesResponse
import com.example.task4module6.presentation.dto.PrizeDto
import com.example.task4module6.presentation.dto.PrizesResponse
import com.example.task4module6.usecase.GetPrizeDetailsUseCase
import com.example.task4module6.usecase.GetPrizeLaureatesUseCase
import com.example.task4module6.usecase.GetPrizesUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.prizeRoutes(
    getPrizesUseCase: GetPrizesUseCase,
    getPrizeDetailsUseCase: GetPrizeDetailsUseCase,
    getPrizeLaureatesUseCase: GetPrizeLaureatesUseCase
) {
    authenticate("auth-jwt") {
        route("/prizes") {
            get {
                val year = call.request.queryParameters["year"]?.toIntOrNull()
                val category = call.request.queryParameters["category"]
                val limit = call.request.queryParameters["limit"]?.toIntOrNull()?.coerceIn(1, 100) ?: 20
                val offset = call.request.queryParameters["offset"]?.toIntOrNull()?.coerceAtLeast(0) ?: 0

                val prizes = getPrizesUseCase(year, category, limit, offset)
                call.respond(
                    PrizesResponse(
                        prizes = prizes.map { it.toDto() },
                        count = prizes.size
                    )
                )
            }

            get("/{year}/{category}") {
                val year = call.parameters["year"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Укажите год", 400)
                )
                val category = call.parameters["category"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Укажите категорию", 400)
                )

                val prize = getPrizeDetailsUseCase(year, category)
                    ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse("Премия не найдена: $year / $category", 404)
                    )

                call.respond(prize.toDto())
            }

            get("/{year}/{category}/laureates") {
                val year = call.parameters["year"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Укажите год", 400)
                )
                val category = call.parameters["category"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Укажите категорию", 400)
                )

                val laureates = getPrizeLaureatesUseCase(year, category)
                    ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse("Премия не найдена: $year / $category", 404)
                    )

                val laureateDtos = laureates.map { it.toDto() }
                call.respond(LaureatesResponse(laureates = laureateDtos, count = laureateDtos.size))
            }
        }
    }
}

private fun Prize.toDto() = PrizeDto(
    year = year,
    category = category,
    categoryFullName = categoryFullName,
    motivation = motivation,
    laureates = laureates.map { it.toDto() }
)

private fun Laureate.toDto() = LaureateDto(
    id = id,
    name = name,
    portion = portion,
    motivation = motivation
)
