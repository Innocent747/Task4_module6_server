package com.example.task4module6.service

import com.example.task4module6.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

class NobelService(private val client: HttpClient) {

    private val BASE_URL = "https://api.nobelprize.org/2.1"

    // Simple in-memory cache
    private var cachedPrizes: List<NobelPrize>? = null
    private var lastFetchTime: Long = 0
    private val cacheTtlMs = 5 * 60 * 1000L // 5 minutes

    private suspend fun fetchAll(): List<NobelPrize> {
        val now = System.currentTimeMillis()
        if (cachedPrizes != null && (now - lastFetchTime) < cacheTtlMs) {
            return cachedPrizes!!
        }

        val prizes = mutableListOf<NobelPrize>()
        var offset = 0
        val limit = 100

        while (true) {
            val response = client.get("$BASE_URL/nobelPrizes") {
                parameter("limit", limit)
                parameter("offset", offset)
            }.body<NobelPrizesResponse>()

            prizes.addAll(response.nobelPrizes)
            if (response.nobelPrizes.size < limit) break
            offset += limit
        }

        cachedPrizes = prizes
        lastFetchTime = now
        return prizes
    }

    suspend fun getPrizes(
        year: Int? = null,
        category: String? = null,
        limit: Int = 20,
        offset: Int = 0
    ): List<PrizeResponse> {
        val all = fetchAll()
        return all
            .filter { p -> year == null || p.awardYear == year.toString() }
            .filter { p -> category == null || p.category?.en?.lowercase() == category.lowercase() }
            .drop(offset)
            .take(limit)
            .map { it.toPrizeResponse() }
    }

    suspend fun getPrizeByYearAndCategory(year: String, category: String): PrizeResponse? {
        val all = fetchAll()
        return all.find {
            it.awardYear == year && it.category?.en?.lowercase() == category.lowercase()
        }?.toPrizeResponse()
    }

    suspend fun getCategories(): List<String> {
        val all = fetchAll()
        return all.mapNotNull { it.category?.en }.distinct().sorted()
    }

    suspend fun getYears(): List<String> {
        val all = fetchAll()
        return all.map { it.awardYear }.distinct().sortedDescending()
    }

    private fun NobelPrize.toPrizeResponse() = PrizeResponse(
        year = awardYear,
        category = category?.en ?: "",
        categoryFullName = categoryFullName?.en ?: category?.en ?: "",
        motivation = topMotivation?.en ?: "",
        laureates = laureates?.map { l ->
            LaureateResponse(
                id = l.id,
                name = l.fullName?.en ?: l.knownName?.en ?: "Организация",
                portion = l.portion ?: "1",
                motivation = l.motivation?.en ?: topMotivation?.en ?: ""
            )
        } ?: emptyList()
    )
}
