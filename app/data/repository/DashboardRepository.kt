package com.ai.franchise.data.repository

import android.content.Context
import com.ai.franchise.data.api.DashboardApi
import com.ai.franchise.data.remote.ApiClient
import com.ai.franchise.ui.dashboard.OwnerStats
import com.ai.franchise.utils.NetworkUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DashboardRepository(private val context: Context) {

    private val api: DashboardApi by lazy { ApiClient.getClient(context).create(DashboardApi::class.java) }
    private val gson = Gson()
    private val cacheFile = File(context.cacheDir, "dashboard_owner_cache.json")

    suspend fun getOwnerStats(): OwnerStats {
        return withContext(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(context)) {
                try {
                    val response = api.getOwnerStats()
                    if (response.isSuccessful && response.body() != null) {
                        val stats = response.body()!!
                        // Cache it
                        cacheFile.writeText(gson.toJson(stats))
                        stats
                    } else {
                        loadFromCacheOrThrow()
                    }
                } catch (e: Exception) {
                    loadFromCacheOrThrow()
                }
            } else {
                loadFromCacheOrThrow()
            }
        }
    }

    private fun loadFromCacheOrThrow(): OwnerStats {
        if (cacheFile.exists()) {
            return gson.fromJson(cacheFile.readText(), OwnerStats::class.java)
        }
        // Fallback Mock if no cache exists yet (First run offline)
        return OwnerStats("0", "0", "0", "MOCK DATA (OFFLINE)")
    }
}
