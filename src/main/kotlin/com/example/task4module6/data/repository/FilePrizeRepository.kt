package com.example.task4module6.data.repository

import com.example.task4module6.data.source.ResourcePrizeDataSource
import com.example.task4module6.domain.model.Laureate
import com.example.task4module6.domain.model.Prize
import com.example.task4module6.domain.repository.PrizeRepository

class FilePrizeRepository(
    dataSource: ResourcePrizeDataSource
) : PrizeRepository {
    private val prizes: List<Prize> = dataSource.loadPrizes()

    override fun getPrizes(year: Int?, category: String?, limit: Int, offset: Int): List<Prize> {
        return prizes
            .asSequence()
            .filter { prize -> year == null || prize.year == year.toString() }
            .filter { prize -> category == null || prize.category.equals(category, ignoreCase = true) }
            .drop(offset)
            .take(limit)
            .toList()
    }

    override fun getPrizeByYearAndCategory(year: String, category: String): Prize? {
        return prizes.firstOrNull { prize ->
            prize.year == year && prize.category.equals(category, ignoreCase = true)
        }
    }

    override fun getLaureatesByYearAndCategory(year: String, category: String): List<Laureate>? {
        return getPrizeByYearAndCategory(year, category)?.laureates
    }
}
