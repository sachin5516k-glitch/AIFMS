package com.ai.franchise.data.repository

import android.content.Context
import com.ai.franchise.data.api.InventoryApi
import com.ai.franchise.data.remote.ApiClient
import com.ai.franchise.ui.inventory.InventoryItem
import com.ai.franchise.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InventoryRepository(private val context: Context) {
    
    private val api: InventoryApi by lazy { ApiClient.getClient(context).create(InventoryApi::class.java) }

    suspend fun getInventory(): List<InventoryItem> {
        return withContext(Dispatchers.IO) {
            if (!NetworkUtils.isInternetAvailable(context)) {
               // Return Cached or Mock
               return@withContext MockData.inventory
            }
            try {
                val response = api.getInventory()
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!
                } else {
                    MockData.inventory // Fallback
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun submitInventory(items: List<InventoryItem>): Boolean {
        return withContext(Dispatchers.IO) {
             if (!NetworkUtils.isInternetAvailable(context)) {
                throw Exception("Offline. Saved to Outbox.")
            }
            try {
                val response = api.submitInventory(items)
                if (!response.isSuccessful) throw Exception("Sync Failed")
                true
            } catch (e: Exception) {
                throw e
            }
        }
    }
    
    object MockData {
        val inventory = listOf(
            InventoryItem("1", "Coffee Beans (kg)", 50.0),
            InventoryItem("2", "Milk (L)", 20.0),
            InventoryItem("3", "Sugar (kg)", 10.0),
            InventoryItem("4", "Cups (pcs)", 500.0)
        )
    }
}
