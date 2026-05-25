package com.example.task4module6.usecase

import com.example.task4module6.domain.model.Laureate
import com.example.task4module6.domain.repository.PrizeRepository

class GetPrizeLaureatesUseCase(private val prizeRepository: PrizeRepository) {
    operator fun invoke(year: String, category: String): List<Laureate>? {
        return prizeRepository.getLaureatesByYearAndCategory(year, category)
    }
}
