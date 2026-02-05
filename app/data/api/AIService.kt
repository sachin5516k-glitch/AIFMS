package com.ai.franchise.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST

data class AIScore(
    val franchiseId: String,
    val fraudScore: Int,
    val healthScore: Int,
    val failureRisk: Int,
    val anomalies: List<String>,
    val recommendations: List<String>
)

interface AIService {
    @POST("ai/analyze")
    suspend fun runAnalysis(): Response<AIScore>

    @GET("ai/insights/{franchiseId}")
    suspend fun getInsights(@Path("franchiseId") franchiseId: String): Response<AIScore>
}
