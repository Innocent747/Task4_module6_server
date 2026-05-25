package com.example.task4module6.usecase

import com.example.task4module6.domain.model.Prize
import com.example.task4module6.domain.repository.PrizeRepository

class GetPrizeDetailsUseCase(private val prizeRepository: PrizeRepository) {
    operator fun invoke(year: String, category: String): Prize? {
        return prizeRepository.getPrizeByYearAndCategory(year, category)
    }
}
