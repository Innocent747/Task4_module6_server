package com.example.task4module6.data.source

import com.example.task4module6.data.model.PrizeFile
import com.example.task4module6.domain.model.Laureate
import com.example.task4module6.domain.model.Prize
import kotlinx.serialization.json.Json

class ResourcePrizeDataSource {
    private val json = Json { ignoreUnknownKeys = true }

    fun loadPrizes(): List<Prize> {
        val stream = javaClass.classLoader.getResourceAsStream("prizes.json")
            ?: error("Resource not found: prizes.json")
        val content = stream.bufferedReader().use { it.readText() }
        val prizeFile = json.decodeFromString<PrizeFile>(content)

        return prizeFile.prizes.map { prize ->
            Prize(
                year = prize.year,
                category = prize.category,
                categoryFullName = prize.categoryFullName,
                motivation = prize.motivation,
                laureates = prize.laureates.map { laureate ->
                    Laureate(
                        id = laureate.id,
                        name = laureate.name,
                        portion = laureate.portion,
                        motivation = laureate.motivation
                    )
                }
            )
        }
    }
}
