package com.ipt2025.project_dam.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class UserLoginRequest(
    val email: String,
    val password: String
)

data class UserData(
    val role: String,
    val name: String
)

data class UserLoginResponse(
    val token: String,
    val user: UserData
)

interface LoginAPIService {
    @POST("auth/login")
    suspend fun loginUser(@Body request: UserLoginRequest): UserLoginResponse
}