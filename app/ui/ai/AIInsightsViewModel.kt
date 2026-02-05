package com.ai.franchise.ui.ai

import com.ai.franchise.ui.base.BaseViewModel
import com.ai.franchise.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AIHealthData(
    val score: Int,
    val status: String,
    val risks: List<RiskItem>
)

class AIInsightsViewModel : BaseViewModel<AIHealthData>() {

    fun loadInsights() {
        launchDataLoad {
            kotlinx.coroutines.delay(1000)
            // Mock Response
            AIHealthData(
                score = 85,
                status = "Good Condition",
                risks = listOf(
                    RiskItem("1", "Inventory Mismatch", "Reported stock implies 50 coffees sold, but POS recorded 42.", "Verify closing stock.", 2),
                    RiskItem("2", "Late Opening", "Outlet opened at 09:30 AM instead of 09:00 AM.", "Ensure staff punctuality.", 1)
                )
            )
        }
    }
}
