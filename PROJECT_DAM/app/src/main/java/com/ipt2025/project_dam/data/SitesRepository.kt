package com.ipt2025.project_dam.data

import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SiteDetailResponse
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.data.api.UserLoginResponse
import retrofit2.Retrofit

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class SitesRepository(private val api: SitesAPIService) {

    suspend fun getSiteDetails(siteId: String): SiteDetailResponse {
        return api.getSiteDetails(siteId)
    }
}