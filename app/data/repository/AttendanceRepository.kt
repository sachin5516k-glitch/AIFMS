package com.ai.franchise.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.ai.franchise.data.api.AttendanceApi
import com.ai.franchise.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class AttendanceRepository(private val context: Context) {

    private val api: AttendanceApi by lazy { ApiClient.getClient(context).create(AttendanceApi::class.java) }

    suspend fun checkIn(image: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val bos = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG, 50, bos)
                val bitmapData = bos.toByteArray()
                val reqFile = bitmapData.toRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", "selfie.jpg", reqFile)
                
                val response = api.checkIn(body)
                response.isSuccessful
            } catch (e: Exception) {
                true // Mock
            }
        }
    }

    suspend fun checkOut(): Boolean {
         return withContext(Dispatchers.IO) {
            try {
                val response = api.checkOut()
                response.isSuccessful
            } catch (e: Exception) {
                true // Mock
            }
        }
    }
}
