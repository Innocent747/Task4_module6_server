package com.example.task4module6.usecase

import com.example.task4module6.domain.model.Prize
import com.example.task4module6.domain.repository.PrizeRepository

class GetPrizesUseCase(private val prizeRepository: PrizeRepository) {
    operator fun invoke(year: Int?, category: String?, limit: Int, offset: Int): List<Prize> {
        return prizeRepository.getPrizes(year, category, limit, offset)
    }
}
