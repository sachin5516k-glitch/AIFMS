package com.ai.franchise.data.remote

import com.ai.franchise.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        
        tokenManager.getToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        
        val response = chain.proceed(requestBuilder.build())
        
        if (response.code == 401) {
            // Token expired or invalid
            tokenManager.clearToken()
            // In a real app, we would broadcast an event to open LoginActivity here
        }
        
        return response
    }
}
