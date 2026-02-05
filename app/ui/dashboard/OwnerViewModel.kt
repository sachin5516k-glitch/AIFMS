package com.ai.franchise.ui.dashboard

import com.ai.franchise.data.repository.ReportsRepository
import com.ai.franchise.ui.base.BaseViewModel
import com.ai.franchise.ui.base.UiState

data class OwnerStats(
    val totalRevenue: Double,
    val activeOutlets: Int,
    val highRiskCount: Int,
    val revenueTrend: List<Float>,
    val riskDistribution: List<Float>
)

class OwnerViewModel : BaseViewModel<OwnerStats>() {
    
    // In real app, inject Repo. Simulating data for now.
    fun loadData() {
        launchDataLoad {
            // Simulate API delay
            kotlinx.coroutines.delay(1000)
            
            // Mock Data
            OwnerStats(
                totalRevenue = 150000.0,
                activeOutlets = 12,
                highRiskCount = 2,
                revenueTrend = listOf(10f, 20f, 15f, 30f, 40f, 35f, 50f),
                riskDistribution = listOf(80f, 15f, 5f) // Safe, Warning, Critical
            )
        }
    }
}
