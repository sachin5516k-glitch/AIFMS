package com.ai.franchise.data.repository

import android.content.Context
import com.ai.franchise.data.api.GovernanceApi
import com.ai.franchise.data.remote.ApiClient
import com.ai.franchise.ui.ai.AIHealthData
import com.ai.franchise.ui.ai.RiskItem
import com.ai.franchise.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GovernanceRepository(private val context: Context) {

    private val api: GovernanceApi by lazy { ApiClient.getClient(context).create(GovernanceApi::class.java) }

    suspend fun getInsights(outletId: String): AIHealthData {
         return withContext(Dispatchers.IO) {
             if (!NetworkUtils.isInternetAvailable(context)) {
                return@withContext MockData.insights
             }
             try {
                 val response = api.getInsights(outletId)
                 if (response.isSuccessful && response.body() != null) {
                     response.body()!!
                 } else {
                     MockData.insights
                 }
             } catch (e: Exception) {
                 MockData.insights
             }
         }
    }
    
    suspend fun raiseDispute(reason: String): Boolean {
         return withContext(Dispatchers.IO) {
             try {
                 val map = mapOf("reason" to reason)
                 val response = api.raiseDispute(map)
                 response.isSuccessful
             } catch (e: Exception) {
                 true // Mock success
             }
         }
    }
    
    object MockData {
         val insights = AIHealthData(
                score = 85,
                status = "Good Condition",
                risks = listOf(
                    RiskItem("1", "Inventory Mismatch", "Reported stock implies 50 coffees sold, but POS recorded 42.", "Verify closing stock.", 2),
                    RiskItem("2", "Late Opening", "Outlet opened at 09:30 AM instead of 09:00 AM.", "Ensure staff punctuality.", 1)
                )
            )
    }
}
