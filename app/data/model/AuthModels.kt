package com.ai.franchise.data.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val username: String,
    val password: String,
    val role: String,
    val franchiseId: String? = null
)

data class LoginResponse(
    val _id: String,
    val name: String,
    val username: String,
    val role: String,
    val franchiseId: String?,
    val token: String
)
