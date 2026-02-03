package com.ipt2025.project_dam.data.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class UserLoginRequest(
    val email: String,
    val password: String
)

data class UserLoginTokenRequest(
    val token: String,
)

data class UserData(
    val role: String,
    val name: String,
    val email: String? = null,
    val id: String
)

data class UserDetailsData(
    val role: String,
    val name: String,
    val email: String
)

data class UserLoginResponse(
    val token: String,
    val user: UserData
)

data class RegisterUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String
)

data class RegisterUserResponse(
    val _id: String,
    val name: String,
    val email: String,
    val role: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
)

data class EditUserRequest(
    val name: String,
    val email: String,
    val password: String? = null,  // (optional) only if changing password
    val role: String
)

data class EditUserResponse(
    val _id: String,
    val name: String,
    val email: String,
    val role: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
)

data class DeleteUserResponse(
    val message: String
)

// user list pagination response
data class UsersListResponse(
    val users: List<UserData>,
    val total: Int,
    val page: Int,
    val limit: Int
)

interface LoginAPIService {
    // perform login / get access JWT token
    @POST("auth/login")
    suspend fun loginUser(@Body request: UserLoginRequest): UserLoginResponse

    // get account details for current logged in user
    @GET("users/profile")
    suspend fun loginToken(): UserData
}

interface UsersAPIService {
    // get paginated list of users
    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): UsersListResponse

    // get specific user details
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): UserDetailsData

    // create user (admin only)
    @POST("users")
    suspend fun registerUser(@Body request: RegisterUserRequest): RegisterUserResponse

    // update user details
    @PUT("users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: String,
        @Body request: EditUserRequest
    ): EditUserResponse

    // soft delete user
    @DELETE("users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: String): DeleteUserResponse
}