package com.ai.franchise.data.config

import com.ai.franchise.data.local.TokenManager

object FeatureFlagManager {
    
    // Feature Keys
    const val FEATURE_AI_INSIGHTS = "feature_ai_insights"
    const val FEATURE_OFFLINE_MODE = "feature_offline_mode"
    const val FEATURE_GEO_FENCING = "feature_geo_fencing"
    const val FEATURE_PUSH_NOTIFICATIONS = "feature_push_notifications"
    
    // Default States (Remote Config Fallback)
    private val defaults = mapOf(
        FEATURE_AI_INSIGHTS to true,
        FEATURE_OFFLINE_MODE to true,
        FEATURE_GEO_FENCING to true,
        FEATURE_PUSH_NOTIFICATIONS to false // Free tier limit
    )

    fun isEnabled(featureKey: String, tokenManager: TokenManager): Boolean {
        val role = tokenManager.getUserRole()
        
        // Role-Based Overrides
        if (role == "OWNER") {
            // Owners get everything
            return true
        }
        
        if (role == "STAFF" && featureKey == FEATURE_AI_INSIGHTS) {
            // Staff might typically not see high-level AI insights
            return false
        }
        
        return defaults[featureKey] ?: false
    }
}
