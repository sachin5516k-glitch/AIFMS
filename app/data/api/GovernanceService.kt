package com.ai.franchise.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming
import okhttp3.ResponseBody

data class DisputeRequest(
    val type: String,
    val referenceId: String,
    val reason: String
)

interface GovernanceService {
    @POST("governance/dispute")
    suspend fun raiseDispute(@Body request: DisputeRequest): Response<Any>

    @Streaming
    @GET("reports/audit/{franchiseId}/download")
    suspend fun downloadAuditReport(@Path("franchiseId") franchiseId: String): Response<ResponseBody>
}
