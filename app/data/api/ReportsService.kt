package com.ai.franchise.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class RoyaltyResponse(
    val period: String,
    val data: List<RoyaltyData>
)

data class RoyaltyData(
    val franchiseName: String,
    val totalSales: Double,
    val transactionCount: Int,
    val royaltyPercentage: Double,
    val calculatedRoyalty: Double
)

interface ReportsService {
    @GET("reports/royalty")
    suspend fun getRoyaltyReport(
        @Query("month") month: Int?,
        @Query("year") year: Int?
    ): Response<RoyaltyResponse>
}
