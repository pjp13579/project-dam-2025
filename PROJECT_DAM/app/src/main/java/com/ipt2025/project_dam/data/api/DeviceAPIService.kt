package com.ipt2025.project_dam.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date


    data class DevicesResponse(
        val pages: Int,
        val total: Int,
        val devices: List<DeviceDataResponse>
    )


    data class DeviceDataResponse(
        val _id: String,
        val vendor: String,
        val category: String,
        val type: String,
        val serialNumber: String,
        val macAddress: String,
        val state: String,
        val site: String,
        val createdAt: Date,
        val updatedAt: Date
    )


interface DevicesAPIService {
    @GET("devices")
    suspend fun getDevices(@Query("page") page: Int, @Query("limit") limit: Int): DevicesResponse
}