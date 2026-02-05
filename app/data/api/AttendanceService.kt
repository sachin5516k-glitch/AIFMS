package com.ai.franchise.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AttendanceService {
    @Multipart
    @POST("attendance/checkin")
    suspend fun checkIn(
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part selfie: MultipartBody.Part
    ): Response<Any>

    @Multipart
    @POST("attendance/checkout")
    suspend fun checkOut(
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part selfie: MultipartBody.Part
    ): Response<Any>
}
