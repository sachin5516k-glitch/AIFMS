package com.ai.franchise.di

import com.ai.franchise.data.api.AuthService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:5000/api/" // 10.0.2.2 for Android Emulator

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authService: AuthService = retrofit.create(AuthService::class.java)
    val salesService: SalesService = retrofit.create(SalesService::class.java)
    val attendanceService: AttendanceService = retrofit.create(AttendanceService::class.java)
    val inventoryService: InventoryService = retrofit.create(InventoryService::class.java)
    val reportsService: ReportsService = retrofit.create(ReportsService::class.java)
    val aiService: AIService = retrofit.create(AIService::class.java)
    val governanceService: GovernanceService = retrofit.create(GovernanceService::class.java)
}
