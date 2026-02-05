package com.ai.franchise.data.api

import com.ai.franchise.data.model.InventoryItem
import com.ai.franchise.data.model.InventoryLogRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface InventoryService {
    @GET("inventory/items")
    suspend fun getItems(): Response<List<InventoryItem>>

    @POST("inventory/log")
    suspend fun submitLog(@Body request: InventoryLogRequest): Response<Any>
}
