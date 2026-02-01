package com.ipt2025.project_dam.data.api

import com.ipt2025.project_dam.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * singleton object that configures the http client
 *
 * inject JWT token into header
 */
object RetrofitProvider {
    private var authToken: String? = null
    private var user : UserData? = null


    // stores the token in memory
    fun updateToken(token: String) {
        authToken = token
    }

    // fetch token
    fun getToken() : String?{
        return authToken;
    }

    // wipe token
    fun clearToken() {
        authToken = null
    }

    // get the current user's role (specified in the JWT token)
    fun getUserRole(): String? {
        return user?.role
    }

    // role checking
    fun isAdmin(): Boolean {
        return user?.role?.equals("admin", ignoreCase = true) ?: false
    }

    fun isTech(): Boolean {
        return user?.role?.equals("technician", ignoreCase = true) ?: false
    }

    fun isGuest(): Boolean {
        return user?.role?.equals("guest", ignoreCase = true) ?: false
    }

    fun canViewDevices(): Boolean {
        return isAdmin() || isTech() || isGuest()
    }

    fun canViewSites(): Boolean {
        return isAdmin() || isTech() || isGuest()
    }

    fun canViewUsers(): Boolean {
        return isAdmin()
    }

    fun canCreateDevices(): Boolean {
        return isAdmin() || isTech()
    }

    fun canCreateSite(): Boolean {
        return isAdmin() || isTech()
    }

    fun canCreateUser(): Boolean {
        return isAdmin()
    }

    fun canEditDevice(): Boolean {
        return isAdmin() || isTech()
    }

    fun canEditSite(): Boolean {
        return isAdmin() || isTech()
    }

    fun canEditUser(): Boolean {
        return isAdmin()
    }

    fun canDeleteDevice(): Boolean {
        return isAdmin() || isTech()
    }

    fun canDeleteSite(): Boolean {
        return isAdmin() || isTech()
    }

    fun canDeleteUser(): Boolean {
        return isAdmin()
    }

    // after successful login, store the provided login/access token
    fun setLoggedInUser(loggedInUser: UserLoginResponse){
        authToken = loggedInUser.token
        user = loggedInUser.user
    }

    // sets up retrofit with a custom interceptor (automatically inject JWT auth token into request header)
    fun <T> create(service: Class<T>): T {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val builder = original.newBuilder()

                // injects the bearer token into every request header if we have one
                authToken?.let {
                    builder.addHeader("Authorization", "Bearer $it")
                }

                val newRequest: Request = builder.build()
                chain.proceed(newRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(service)
    }
}