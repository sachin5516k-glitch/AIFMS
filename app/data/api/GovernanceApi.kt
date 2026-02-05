package com.ai.franchise.data.api

import com.ai.franchise.ui.ai.AIHealthData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GovernanceApi {
    @GET("ai/insights/{outletId}")
    suspend fun getInsights(@Path("outletId") outletId: String): Response<AIHealthData>
    
    @POST("governance/dispute")
    suspend fun raiseDispute(@Body reason: Map<String, String>): Response<Void>
    
    // For download we usually just get raw response body or stream
    @GET("reports/audit/download")
    suspend fun downloadReport(): Response<okhttp3.ResponseBody>
}
