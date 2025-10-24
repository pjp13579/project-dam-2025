package com.ipt2025.project_dam.data.api

import com.ipt2025.project_dam.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    private var authToken: String? = null

    fun updateToken(token: String) {
        authToken = token
    }

    fun clearToken() {
        authToken = null
    }

    fun <T> create(service: Class<T>): T {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val builder = original.newBuilder()

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