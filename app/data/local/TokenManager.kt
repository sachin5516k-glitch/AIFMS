package com.ai.franchise.data.local

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("ai_franchise_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }
    
    fun clearToken() {
        prefs.edit().remove("jwt_token").apply()
    }
    
    fun saveUserRole(role: String) {
        prefs.edit().putString("user_role", role).apply()
    }
    
    fun getUserRole(): String {
        return prefs.getString("user_role", "OUTLET_MANAGER") ?: "OUTLET_MANAGER"
    }
}
