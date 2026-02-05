package com.ai.franchise.data.api

import com.ai.franchise.ui.dashboard.OwnerStats
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DashboardApi {
    @GET("dashboard/owner")
    suspend fun getOwnerStats(): Response<OwnerStats>
    
    // Using map or specific DTO for manager stats
    @GET("dashboard/manager")
    suspend fun getManagerStats(): Response<Map<String, Any>>
    
    @GET("dashboard/outlet/{id}")
    suspend fun getOutletStats(@Path("id") outletId: String): Response<Map<String, Any>>
}
