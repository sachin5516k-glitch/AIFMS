package com.ai.franchise.data.repository

import com.ai.franchise.data.api.AIService
import com.ai.franchise.data.api.AIScore
import com.ai.franchise.di.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AIRepository {
    private val service = NetworkModule.aiService

    suspend fun runAnalysis(): Result<AIScore> {
        return withContext(Dispatchers.IO) {
            try {
                // For Phase 3, we trigger analysis first then show results
                // In real app, this might be a background job
                val response = service.runAnalysis()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Analysis failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getInsights(franchiseId: String): Result<AIScore> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getInsights(franchiseId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to fetch insights"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
