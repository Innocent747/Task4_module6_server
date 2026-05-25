package com.example.task4module6.domain.repository

import com.example.task4module6.domain.model.Laureate
import com.example.task4module6.domain.model.Prize

interface PrizeRepository {
    fun getPrizes(year: Int?, category: String?, limit: Int, offset: Int): List<Prize>
    fun getPrizeByYearAndCategory(year: String, category: String): Prize?
    fun getLaureatesByYearAndCategory(year: String, category: String): List<Laureate>?
}
