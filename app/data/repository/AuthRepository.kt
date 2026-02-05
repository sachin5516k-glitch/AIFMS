package com.ai.franchise.data.repository

import android.content.Context
import com.ai.franchise.data.api.AuthApi
import com.ai.franchise.data.local.TokenManager
import com.ai.franchise.data.model.LoginRequest
import com.ai.franchise.data.model.User
import com.ai.franchise.data.remote.ApiClient
import com.ai.franchise.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val context: Context) {

    private val api: AuthApi by lazy { ApiClient.getClient(context).create(AuthApi::class.java) }
    private val tokenManager: TokenManager by lazy { TokenManager(context) }

    suspend fun login(email: String, password: String): User {
        return withContext(Dispatchers.IO) {
            if (!NetworkUtils.isInternetAvailable(context)) {
                throw Exception("No Internet Connection")
            }

            try {
                val response = api.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    tokenManager.saveToken(user.token)
                    tokenManager.saveUserRole(user.role)
                    user
                } else {
                    throw Exception("Login Failed: ${response.code()} ${response.message()}")
                }
                throw e
            }
        }
    }
    
    fun getToken(): String? = tokenManager.getToken()
    
    fun getUserRole(): String = tokenManager.getUserRole()
    
    fun logout() {
        tokenManager.clearToken()
    }
}
