package com.example.task4module6.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── Upstream Nobel API models (for deserializing from api.nobelprize.org) ──

@Serializable
data class NobelPrizesResponse(
    @SerialName("nobelPrizes") val nobelPrizes: List<NobelPrize> = emptyList()
)

@Serializable
data class NobelPrize(
    @SerialName("awardYear") val awardYear: String = "",
    @SerialName("category") val category: CategoryName? = null,
    @SerialName("categoryFullName") val categoryFullName: LocalizedName? = null,
    @SerialName("dateAwarded") val dateAwarded: String? = null,
    @SerialName("prizeAmount") val prizeAmount: Long? = null,
    @SerialName("topMotivation") val topMotivation: LocalizedName? = null,
    @SerialName("laureates") val laureates: List<Laureate>? = emptyList()
)

@Serializable
data class CategoryName(
    @SerialName("en") val en: String = ""
)

@Serializable
data class LocalizedName(
    @SerialName("en") val en: String? = null,
    @SerialName("no") val no: String? = null,
    @SerialName("se") val se: String? = null
)

@Serializable
data class Laureate(
    @SerialName("id") val id: String = "",
    @SerialName("knownName") val knownName: LocalizedName? = null,
    @SerialName("fullName") val fullName: LocalizedName? = null,
    @SerialName("portion") val portion: String? = null,
    @SerialName("motivation") val motivation: LocalizedName? = null
)

// ── Our simplified API response models ────────────────────────────────────

@Serializable
data class PrizeResponse(
    val year: String,
    val category: String,
    val categoryFullName: String,
    val motivation: String,
    val laureates: List<LaureateResponse>
)

@Serializable
data class LaureateResponse(
    val id: String,
    val name: String,
    val portion: String,
    val motivation: String
)

@Serializable
data class ErrorResponse(
    val error: String,
    val status: Int
)
