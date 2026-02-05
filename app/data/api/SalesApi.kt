package com.ai.franchise.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SalesApi {
    @Multipart
    @POST("sales")
    suspend fun submitSales(
        @Part("amount") amount: RequestBody,
        @Part("date") date: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Void>
}
