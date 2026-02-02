package com.ipt2025.project_dam.data

import com.ipt2025.project_dam.BuildConfig
import com.ipt2025.project_dam.data.api.LoginAPIService
import com.ipt2025.project_dam.data.api.UserLoginRequest
import com.ipt2025.project_dam.data.api.UserLoginResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * handles authentication with login credentials.
 *
 * Doesn't inject JWT token into header
 */
class LoginDataSource {

    /**
     * calls the api with username and password
     */
    suspend fun login(username: String, password: String): Result<UserLoginResponse> {
        try {
            // build a specific retrofit instance for login since we don't have a JWT auth token yet
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL) // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(LoginAPIService::class.java)
            val request = UserLoginRequest(username, password)
            val result = apiService.loginUser(request)
            return Result.Success(result)
        } catch (e: Throwable) {
            // wrap failure in result class
            return Result.Error(IOException("Error logging in", e))
        }
    }
}