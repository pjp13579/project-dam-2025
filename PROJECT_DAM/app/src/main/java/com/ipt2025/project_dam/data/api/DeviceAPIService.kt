package com.ipt2025.project_dam.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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
    val site: String,  // Site ID as string in list view
    val isActive: Boolean,
    val createdAt: Date,
    val updatedAt: Date
)

data class DeviceDetailDataResponse(
    val _id: String,
    val vendor: String,
    val category: String,
    val type: String,
    val serialNumber: String,
    val macAddress: String,
    val state: String,
    val isActive: Boolean,
    val site: DeviceDetailSiteResponse,  // Full site object in detail view
    val connectedDevices: List<DeviceDetailsConnectedDeviceResponse>,
    val createdAt: Date,
    val updatedAt: Date
)

data class DeviceDetailSiteResponse(
    val _id: String,
    val type: String,
    val country: String,
    val address: DeviceDetailsSiteAddressResponse
)

data class DeviceDetailsSiteAddressResponse(
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val latitude: Float,
    val longitude: Float,
    val _id: String
)

data class DeviceDetailsConnectedDeviceResponse(
    val _id: String,
    val vendor: String,
    val category: String,
    val type: String,
    val serialNumber: String,
    val macAddress: String,
    val state: String,
    val isActive: Boolean,
    val site: DeviceDetailSiteResponse,
    val createdAt: Date,
    val updatedAt: Date
)

// For CREATE request - site should be a String (ID)
data class DeviceCreateRequest(
    val vendor: String,
    val category: String,
    val type: String,
    val serialNumber: String,
    val macAddress: String,
    val state: String,
    val site: String,  // Site ID as string
    val connectedDevices: List<String>? = emptyList(),
    val isActive: Boolean = true  // Default to active
)

// For UPDATE request - all fields optional except isActive
data class DeviceUpdateRequest(
    val vendor: String? = null,
    val category: String? = null,
    val type: String? = null,
    val serialNumber: String? = null,
    val macAddress: String? = null,
    val state: String? = null,
    val site: String? = null,  // Site ID as string
    val connectedDevices: List<String>? = null,
    val isActive: Boolean? = null
)
data class DeviceResponseSite(
    val _id: String,
    val type: String? = null,
    val country: String? = null
)


data class DeviceCreateUpdateResponse(
    val _id: String,
    val vendor: String,
    val category: String,
    val type: String,
    val serialNumber: String,
    val macAddress: String,
    val state: String,
    val isActive: Boolean,
    val site: DeviceResponseSite,
    val createdAt: Date,
    val updatedAt: Date
)



interface DevicesAPIService {
    @GET("devices")
    suspend fun getDevices(@Query("page") page: Int, @Query("limit") limit: Int): DevicesResponse

    @GET("devices/{deviceId}")
    suspend fun getDeviceDetails(@Path("deviceId") deviceId: String?): DeviceDetailDataResponse

    // Create device - expects site as String ID
    @POST("devices")
    suspend fun createDevice(@Body device: DeviceCreateRequest): Response<DeviceCreateUpdateResponse>

    // Update device - expects site as String ID
    @PUT("devices/{deviceId}")
    suspend fun updateDevice(
        @Path("deviceId") deviceId: String,
        @Body device: DeviceUpdateRequest
    ): Response<DeviceCreateUpdateResponse>

    // Delete device
    @DELETE("devices/{deviceId}")
    suspend fun deleteDevice(@Path("deviceId") deviceId: String): Response<Unit>
}