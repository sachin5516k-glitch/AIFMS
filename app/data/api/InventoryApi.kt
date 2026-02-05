package com.ai.franchise.data.api

import com.ai.franchise.ui.inventory.InventoryItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface InventoryApi {
    @GET("inventory")
    suspend fun getInventory(): Response<List<InventoryItem>>
    
    @POST("inventory")
    suspend fun submitInventory(@Body items: List<InventoryItem>): Response<Void>
}
