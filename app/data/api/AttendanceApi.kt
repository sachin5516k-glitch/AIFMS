package com.ai.franchise.data.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AttendanceApi {
    @Multipart
    @POST("attendance/checkin")
    suspend fun checkIn(@Part image: MultipartBody.Part): Response<Void>
    
    @POST("attendance/checkout")
    suspend fun checkOut(): Response<Void>
}
