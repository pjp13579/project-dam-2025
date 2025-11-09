package com.ipt2025.project_dam.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

data class SitesResponse(
    val pages: Int,
    val total: Int,
    val sites: List<SiteDataResponse>
)

data class SiteDataResponse(
    val _id: String,
    val localName: String,
    val type: String,
    val country: String,
    val address: SiteAddressResponse?,
    val isActive: Boolean,
    val createdAt: Date,
    val updatedAt: Date
)

data class SiteAddressResponse(
    val street : String,
    val city : String,
    val state : String,
    val zipCode: String,
    val latitude: Float,
    val longitude: Float
)

data class SiteDeviceDataResponse(
    val _id: String,
    val vendor: String,
    val category: String,
    val type: String,
    val serialNumber: String,
    val macAddress: String,
    val state: String,
    val site: DeviceDetailSiteResponse,
    val createdAt: Date,
    val updatedAt: Date
)


data class SiteDetailResponse(
    val _id: String,
    val localName: String,
    val type: String,
    val country: String,
    val address: SiteAddressResponse?,
    val devicesAtSite: List<SiteDeviceDataResponse>?,
    val isActive: Boolean,
    val createdAt: Date,
    val updatedAt: Date
)

interface SitesAPIService {
    @GET("sites")
    suspend fun getSites(@Query("page") page: Int, @Query("limit") limit: Int): SitesResponse

    @GET("sites/{siteId}")
    suspend fun getSiteDetails(@Path("siteId") siteId: String): SiteDetailResponse
}